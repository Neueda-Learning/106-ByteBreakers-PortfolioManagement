import { useMemo } from "react";

import { Group, SimpleGrid, Text } from "@mantine/core";

import {
  ArcElement,
  Chart as ChartJS,
  Legend,
  Tooltip as ChartTooltip,
} from "chart.js";

import { Pie } from "react-chartjs-2";

import {
  type ColumnDef,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";

import {
  DataTable,
  SectionCard,
  StatCard,
  StatusBadge,
} from "@/components/common";
import { PageContainer } from "@/components/layout";
import {
  type RecentInvestment,
  usePortfolioSummary,
} from "@/hooks/usePortfolioSummary";

ChartJS.register(ArcElement, ChartTooltip, Legend);

const formatCurrency = (value: number) =>
  `₹${value.toLocaleString("en-IN", {
    maximumFractionDigits: 2,
  })}`;

const Dashboard = () => {
  const { summary, loading } = usePortfolioSummary();

  const allocationEntries = useMemo(
    () => Object.entries(summary?.allocationByCategory ?? {}),
    [summary],
  );

  const chartData = useMemo(
    () => ({
      labels: allocationEntries.map(([category]) => category),
      datasets: [
        {
          data: allocationEntries.map(([, value]) => value),
          backgroundColor: [
            "#1E3A8A",
            "#0284C7",
            "#16A34A",
            "#F59E0B",
            "#EF4444",
            "#7C3AED",
            "#9333EA",
          ],
          borderColor: "#FFFFFF",
          borderWidth: 2,
        },
      ],
    }),
    [allocationEntries],
  );

  const recentColumns = useMemo<ColumnDef<RecentInvestment>[]>(
    () => [
      {
        accessorKey: "name",
        header: "Investment",
      },
      {
        accessorKey: "category",
        header: "Category",
        cell: ({ row }) => (
          <StatusBadge color="blue">{row.original.category}</StatusBadge>
        ),
      },
      {
        accessorKey: "action",
        header: "Action",
        cell: ({ row }) => {
          const action = String(row.original.action || "").toUpperCase();

          return (
            <StatusBadge color={action === "BUY" ? "green" : "red"}>
              {action}
            </StatusBadge>
          );
        },
      },
      {
        accessorKey: "quantity",
        header: "Quantity",
        meta: { align: "right" },
        cell: ({ row }) =>
          Number(row.original.quantity).toLocaleString("en-IN", {
            maximumFractionDigits: 6,
          }),
      },
      {
        accessorKey: "price",
        header: "Price",
        meta: { align: "right" },
        cell: ({ row }) => formatCurrency(Number(row.original.price)),
      },
      {
        accessorKey: "date",
        header: "Date",
        cell: ({ row }) => {
          const value = row.original.date;
          const parsedDate = new Date(value);

          if (Number.isNaN(parsedDate.getTime())) {
            return value;
          }

          return parsedDate.toLocaleDateString("en-IN");
        },
      },
    ],
    [],
  );

  const recentTable = useReactTable({
    data: summary?.recentInvestments ?? [],
    columns: recentColumns,
    getCoreRowModel: getCoreRowModel(),
  });

  const profitLoss = summary?.totalProfitLoss ?? 0;
  const profitTrend = profitLoss >= 0 ? "positive" : "negative";

  return (
    <PageContainer title="Dashboard">
      <SimpleGrid cols={{ base: 1, lg: 2 }} spacing="md" mb="md">
        <SectionCard
          title="Investment Summary"
          subtitle="Portfolio performance overview"
        >
          {loading ? (
            <Text c="dimmed">Loading summary...</Text>
          ) : (
            <SimpleGrid cols={1} spacing="md">
              <StatCard
                title="Total Investments"
                value={formatCurrency(summary?.totalInvestment ?? 0)}
              />

              <StatCard
                title="Current Value"
                value={formatCurrency(summary?.currentValue ?? 0)}
              />

              <StatCard
                title="Total Returns"
                value={formatCurrency(profitLoss)}
                change={`${
                  (summary?.totalProfitLossPct ?? 0) >= 0 ? "+" : ""
                }${(summary?.totalProfitLossPct ?? 0).toFixed(2)}%`}
                trend={profitTrend}
              />
            </SimpleGrid>
          )}
        </SectionCard>

        <SectionCard title="Allocation by Category">
          {loading ? (
            <Text c="dimmed">Loading chart...</Text>
          ) : allocationEntries.length === 0 ? (
            <Text c="dimmed">No allocation data found.</Text>
          ) : (
            <Group justify="center" py="sm">
              <div style={{ width: "100%", height: 340 }}>
                <Pie
                  data={chartData}
                  options={{
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                      legend: {
                        position: "right",
                        labels: {
                          font: {
                            size: 14,
                          },
                          padding: 16,
                          generateLabels: (chart) => {
                            const data = chart.data;
                            const dataset = data.datasets[0];
                            const total = (dataset.data as number[]).reduce(
                              (sum, val) => sum + (val as number),
                              0,
                            );
                            return (data.labels as string[]).map((label, i) => {
                              const value = dataset.data[i] as number;
                              const pct =
                                total > 0
                                  ? ((value / total) * 100).toFixed(1)
                                  : "0.0";
                              return {
                                text: `${label}  ${pct}%`,
                                fillStyle: (
                                  dataset.backgroundColor as string[]
                                )[i],
                                strokeStyle: dataset.borderColor as string,
                                lineWidth: dataset.borderWidth as number,
                                hidden: false,
                                index: i,
                              };
                            });
                          },
                        },
                        fullSize: true,
                      },
                    },
                  }}
                />
              </div>
            </Group>
          )}
        </SectionCard>
      </SimpleGrid>

      <SectionCard title="Recent Investments">
        {loading ? (
          <Text c="dimmed">Loading recent investments...</Text>
        ) : (summary?.recentInvestments?.length ?? 0) === 0 ? (
          <Text c="dimmed">No recent investments found.</Text>
        ) : (
          <DataTable table={recentTable} />
        )}
      </SectionCard>
    </PageContainer>
  );
};

export default Dashboard;
