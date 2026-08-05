import { useMemo } from "react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import {
  Alert,
  Box,
  Group,
  SimpleGrid,
  Stack,
  Text,
  Title,
} from "@mantine/core";

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  LineElement,
  PointElement,
  Tooltip as ChartTooltip,
  Legend,
} from "chart.js";
import { Line } from "react-chartjs-2";

import {
  IconArrowLeft,
  IconTrendingDown,
  IconTrendingUp,
} from "@tabler/icons-react";

import {
  Button,
  SectionCard,
  showToast,
  TradeQuantityModal,
} from "@/components/common";
import { PageContainer } from "@/components/layout";
import { useInvestmentDetails } from "@/hooks/useInvestmentDetails";
import { useTradeAction, type TradeAction } from "@/hooks/useTradeAction";
import { formatCurrency } from "@/util/currency";

ChartJS.register(
  CategoryScale,
  LinearScale,
  LineElement,
  PointElement,
  ChartTooltip,
  Legend,
);

const toNumber = (value: unknown): number => Number(value ?? 0);

const InvestmentDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const parsedId = Number(id);
  const [tradeAction, setTradeAction] = useState<TradeAction | null>(null);

  const { investment, loading, error, refetch } =
    useInvestmentDetails(parsedId);
  const { executeTrade, submitting } = useTradeAction();

  const handleOpenTrade = (action: TradeAction) => {
    setTradeAction(action);
  };

  const handleConfirmTrade = async (quantity: number) => {
    if (!investment || !tradeAction) {
      return;
    }

    const quantityOwned = toNumber(investment.quantityOwned);
    if (tradeAction === "sell" && quantity > quantityOwned) {
      showToast({
        title: "Invalid sell quantity",
        message: `You only own ${quantityOwned.toLocaleString("en-IN")} units.`,
        tone: "warning",
      });
      return;
    }

    try {
      await executeTrade({
        optionId: investment.id,
        quantity,
        currentPrice: toNumber(investment.currentPrice),
        action: tradeAction,
      });

      showToast({
        title: `${tradeAction === "buy" ? "Buy" : "Sell"} order submitted`,
        message: `${tradeAction === "buy" ? "Bought" : "Sold"} ${quantity} of ${investment.name}.`,
        tone: "success",
      });

      setTradeAction(null);
      await refetch();
    } catch {
      showToast({
        title: "Unable to place order",
        message:
          tradeAction === "sell"
            ? "Sell failed. Ensure you have enough holdings and try again."
            : "Buy failed. Please try again.",
        tone: "error",
      });
    }
  };

  const priceHistoryData = useMemo(() => {
    if (!investment) {
      return null;
    }

    const basePrice = toNumber(investment.currentPrice);
    const estimatedReturnRate = toNumber(investment.estimatedReturn) / 100;
    const trend = String(investment.trend || "").toLowerCase();

    const directionalBias = trend.includes("up")
      ? 0.009
      : trend.includes("down")
        ? -0.009
        : 0;
    const monthlyDrift = estimatedReturnRate / 18 + directionalBias;

    const labels = ["T-7", "T-6", "T-5", "T-4", "T-3", "T-2", "T-1", "Now"];

    const points = labels.map((_, index) => {
      const progress = index - (labels.length - 1);
      const wave = Math.sin(index * 1.25) * basePrice * 0.02;
      const trendMove = basePrice * monthlyDrift * progress;
      const value = basePrice + trendMove + wave;

      return Math.max(0, Number(value.toFixed(2)));
    });

    return {
      labels,
      datasets: [
        {
          label: "Price",
          data: points,
          borderColor: "#1c7ed6",
          backgroundColor: "rgba(28, 126, 214, 0.12)",
          borderWidth: 2,
          tension: 0.35,
          fill: true,
          pointRadius: 3,
          pointHoverRadius: 5,
        },
      ],
    };
  }, [investment]);

  return (
    <PageContainer title="Investment Detail Page">
      <Group>
        <Button
          variant="light"
          leftSection={<IconArrowLeft size={16} />}
          onClick={() => navigate(-1)}
          style={{ marginBottom: "1rem", marginTop: "0rem" }}
        >
          Back
        </Button>
      </Group>

      {loading ? (
        <Text c="dimmed">Loading investment details...</Text>
      ) : error ? (
        <Alert color="red" title="Could not load details">
          {error}
        </Alert>
      ) : !investment ? (
        <Text c="dimmed">No investment details found.</Text>
      ) : (
        <SimpleGrid cols={1} spacing="md">
          <SectionCard>
            <Stack gap="lg">
              <Group justify="space-between" align="flex-start">
                <div>
                  <Title order={2}>{investment.name}</Title>
                  <Text c="dimmed">User investment table</Text>
                </div>

                <Stack gap="xs" align="flex-end">
                  {/* <Text fw={700} c={trendColor(investment.trend)}>
                    {String(investment.trend || "-")
                      .replaceAll("_", " ")
                      .toUpperCase()}
                  </Text> */}

                  <Group>
                    <Button
                      color="green"
                      size="sm"
                      onClick={() => handleOpenTrade("buy")}
                    >
                      Buy
                    </Button>

                    <Button
                      color="red"
                      variant="outline"
                      size="sm"
                      onClick={() => handleOpenTrade("sell")}
                    >
                      Sell
                    </Button>
                  </Group>
                </Stack>
              </Group>

              <SimpleGrid cols={{ base: 1, lg: 2 }} spacing="md">
                <SectionCard title="Details">
                  <Stack gap={4}>
                    <Text size="sm">Category: {investment.category}</Text>
                    <Text size="sm">
                      Current Price: {formatCurrency(investment.currentPrice)}
                    </Text>
                    <Text size="sm">
                      Trend:{" "}
                      {String(investment.trend || "-").replaceAll("_", " ")}
                    </Text>
                    <Text size="sm">
                      Estimated Return:{" "}
                      {toNumber(investment.estimatedReturn).toFixed(1)}%
                    </Text>
                  </Stack>
                </SectionCard>

                <SectionCard title="Your Holdings">
                  <Stack gap={4}>
                    <Text size="sm">
                      Quantity Owned:{" "}
                      {toNumber(investment.quantityOwned).toLocaleString(
                        "en-IN",
                      )}
                    </Text>
                    <Text size="sm">
                      Total Invested: {formatCurrency(investment.totalInvested)}
                    </Text>
                    <Text
                      size="sm"
                      c={
                        toNumber(investment.currentProfitLoss) >= 0
                          ? "green"
                          : "red"
                      }
                    >
                      Current Profit/Loss:{" "}
                      {formatCurrency(investment.currentProfitLoss)}
                    </Text>
                  </Stack>
                </SectionCard>
              </SimpleGrid>

              <SectionCard title="Price History">
                <Stack gap="sm">
                  {priceHistoryData && (
                    <Box h={300}>
                      <Line
                        data={priceHistoryData}
                        options={{
                          responsive: true,
                          maintainAspectRatio: false,
                          plugins: {
                            legend: {
                              display: false,
                            },
                          },
                          scales: {
                            y: {
                              ticks: {
                                callback: (value) =>
                                  formatCurrency(Number(value)),
                              },
                            },
                          },
                        }}
                      />
                    </Box>
                  )}
                </Stack>
              </SectionCard>

              <Group c="dimmed" gap={6}>
                {toNumber(investment.currentProfitLoss) >= 0 ? (
                  <IconTrendingUp size={16} color="green" />
                ) : (
                  <IconTrendingDown size={16} color="red" />
                )}
                <Text size="sm">
                  Holdings performance updates with latest pricing.
                </Text>
              </Group>
            </Stack>
          </SectionCard>
        </SimpleGrid>
      )}

      <TradeQuantityModal
        opened={tradeAction != null && !!investment}
        action={tradeAction ?? "buy"}
        investmentName={investment?.name ?? "Investment"}
        currentPrice={toNumber(investment?.currentPrice)}
        maxQuantity={
          tradeAction === "sell"
            ? toNumber(investment?.quantityOwned)
            : undefined
        }
        submitting={submitting}
        onClose={() => setTradeAction(null)}
        onConfirm={(quantity) => void handleConfirmTrade(quantity)}
      />
    </PageContainer>
  );
};

export default InvestmentDetails;
