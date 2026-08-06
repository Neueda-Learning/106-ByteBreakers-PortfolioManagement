import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  Button,
  Card,
  Group,
  Select,
  SimpleGrid,
  Stack,
  Text,
  Title,
} from "@mantine/core";
import axios from "axios";
import { API_BASE_URL } from "../api/config";

import {
  showToast,
  StatusBadge,
  TradeQuantityModal,
} from "@/components/common";
import { getInvestmentDetailsPath } from "@/constants/routes";
import { useInvestments } from "@/hooks/useInvestments";
import { useTradeAction } from "@/hooks/useTradeAction";

interface DiversifySuggestion {
  optionId: number;
  name: string;
  category: string;
  score: number;
  reason: string;
}

interface SelectedSuggestion {
  optionId: number;
  name: string;
  currentPrice: number;
}

const formatScore = (score: number) => `${(Number(score) * 100).toFixed(1)}%`;

const DiversifyRecommendations = () => {
  const navigate = useNavigate();
  const [topN, setTopN] = useState<string | null>("5");
  const [loading, setLoading] = useState(true);
  const [suggestions, setSuggestions] = useState<DiversifySuggestion[]>([]);
  const [selectedSuggestion, setSelectedSuggestion] =
    useState<SelectedSuggestion | null>(null);
  const { investments } = useInvestments();
  const { executeTrade, submitting } = useTradeAction();

  useEffect(() => {
    const fetchSuggestions = async () => {
      setLoading(true);

      try {
        const response = await axios.get<DiversifySuggestion[]>(
          `${API_BASE_URL}/api/portfolio/diversify`,
          {
            params: {
              topN: Number(topN ?? 5),
            },
          },
        );

        setSuggestions(Array.isArray(response.data) ? response.data : []);
      } catch {
        setSuggestions([]);
      } finally {
        setLoading(false);
      }
    };

    void fetchSuggestions();
  }, [topN]);

  const investmentById = useMemo(() => {
    const map = new Map<number, { currentPrice: number; name: string }>();

    investments.forEach((investment) => {
      map.set(investment.id, {
        currentPrice: Number(investment.currentPrice),
        name: investment.name,
      });
    });

    return map;
  }, [investments]);

  const handleBuyClick = (suggestion: DiversifySuggestion) => {
    const matchedInvestment = investmentById.get(suggestion.optionId);

    if (!matchedInvestment) {
      showToast({
        title: "Unable to place order",
        message: "Price data for this investment is unavailable.",
        tone: "error",
      });
      return;
    }

    setSelectedSuggestion({
      optionId: suggestion.optionId,
      name: suggestion.name,
      currentPrice: matchedInvestment.currentPrice,
    });
  };

  const handleConfirmBuy = async (quantity: number) => {
    if (!selectedSuggestion) {
      return;
    }

    const suggestionToBuy = selectedSuggestion;

    try {
      await executeTrade({
        optionId: suggestionToBuy.optionId,
        quantity,
        currentPrice: suggestionToBuy.currentPrice,
        action: "buy",
      });

      showToast({
        title: "Buy order submitted",
        message: `Bought ${quantity} of ${suggestionToBuy.name}.`,
        tone: "success",
      });

      setSelectedSuggestion(null);
      navigate(getInvestmentDetailsPath(suggestionToBuy.optionId));
    } catch {
      showToast({
        title: "Unable to place order",
        message: "Please try again.",
        tone: "error",
      });
    }
  };

  return (
    <Stack gap="xl">
      <div>
        <Title order={1}>Diversify Recommendations</Title>

        <Text c="dimmed" mt={4}>
          Curated suggestions to improve your portfolio diversification.
        </Text>
      </div>

      <Group justify="space-between" align="center" wrap="wrap">
        <Text c="dimmed">Based on your current holdings and performance.</Text>

        <Select
          label="Number of suggestions"
          value={topN}
          onChange={setTopN}
          data={["3", "5", "7", "10"]}
          w={220}
          variant="filled"
          color="brand"
          radius="md"
        />
      </Group>

      {loading ? (
        <Text c="dimmed">Loading recommendations...</Text>
      ) : suggestions.length === 0 ? (
        <Text c="dimmed">No recommendations found.</Text>
      ) : (
        <SimpleGrid cols={{ base: 1, md: 2 }} spacing="md">
          {suggestions.map((suggestion) => {
            const hasPrice = investmentById.has(suggestion.optionId);

            return (
              <Card
                key={`${suggestion.optionId}-${suggestion.name}`}
                withBorder
                radius="lg"
                shadow="sm"
                p="lg"
              >
                <Stack gap="sm">
                  <Group justify="space-between" align="start">
                    <div>
                      <Text fw={700}>{suggestion.name}</Text>
                      <StatusBadge color="blue">
                        {suggestion.category}
                      </StatusBadge>
                    </div>

                    <StatusBadge color="green">
                      Score {formatScore(suggestion.score)}
                    </StatusBadge>
                  </Group>

                  <Text size="sm" c="dimmed">
                    {suggestion.reason}
                  </Text>

                  <Button
                    color="green"
                    onClick={() => handleBuyClick(suggestion)}
                    disabled={!hasPrice}
                  >
                    Buy
                  </Button>

                  {!hasPrice ? (
                    <Text size="xs" c="red">
                      Unable to buy: current price is not available.
                    </Text>
                  ) : null}
                </Stack>
              </Card>
            );
          })}
        </SimpleGrid>
      )}

      <TradeQuantityModal
        opened={selectedSuggestion != null}
        action="buy"
        investmentName={selectedSuggestion?.name ?? "Investment"}
        currentPrice={selectedSuggestion?.currentPrice ?? 0}
        submitting={submitting}
        onClose={() => setSelectedSuggestion(null)}
        onConfirm={(quantity) => void handleConfirmBuy(quantity)}
      />
    </Stack>
  );
};

export default DiversifyRecommendations;
