import { useMemo, useState } from "react";

import {
  ActionIcon,
  Button,
  Group,
  Text,
  Tooltip,
  Stack,
  Title,
} from "@mantine/core";

import {
  IconAdjustmentsHorizontal,
  IconDiamond,
} from "@tabler/icons-react";

import {
  type ColumnDef,
  getCoreRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useReactTable,
} from "@tanstack/react-table";

import {
  DataTable,
  DataTablePagination,
  SearchBar,
  SectionCard,
  StatusBadge,
} from "@/components/common";
import { useInvestments } from "@/hooks/useInvestments";

interface InvestmentOption {
  id: number;
  name: string;
  category: string;
  currentPrice: number;
  trend: string;
  estimatedReturn: number;
}

const formatCurrency = (value: number) =>
  `₹${value.toLocaleString("en-IN")}`;

const InvestmentOptions = () => {
  const [search, setSearch] = useState("");
  const { investments, loading } = useInvestments();

  const filteredData = useMemo(() => {
    return investments.filter((investment) =>
      `${investment.name} ${investment.category}`
        .toLowerCase()
        .includes(search.toLowerCase())
    );
  }, [investments, search]);

  const columns = useMemo<ColumnDef<InvestmentOption>[]>(
    () => [
      {
        accessorKey: "name",
        header: "Investment",
      },
      {
        accessorKey: "category",
        header: "Category",
        cell: ({ row }) => (
          <StatusBadge color="blue">
            {row.original.category}
          </StatusBadge>
        ),
      },
      {
        accessorKey: "currentPrice",
        header: "Current Price",
        cell: ({ row }) =>
          formatCurrency(row.original.currentPrice),
      },
      {
        accessorKey: "trend",
        header: "Trend",
        cell: ({ row }) => {
          const trend = String(row.original.trend || "").toLowerCase();
          const normalizedTrend = trend.replaceAll("_", " ");

          return (
            <StatusBadge
              color={
                trend.includes("up")
                  ? "green"
                  : trend.includes("down")
                    ? "red"
                    : "gray"
              }
            >
              {normalizedTrend.toUpperCase()}
            </StatusBadge>
          );
        },
      },
      {
        accessorKey: "estimatedReturn",
        header: "Est. Return",
        cell: ({ row }) => (
          <Text fw={600} c="green">
            {row.original.estimatedReturn.toFixed(1)}%
          </Text>
        ),
      },
      {
        id: "buy",
        header: "Actions",
        cell: () => (
          <Button
            size="xs"
            variant="filled"
          >
            Buy
          </Button>
        ),
      },
    ],
    []
  );

  const table = useReactTable({
    data: filteredData,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    initialState: {
      pagination: {
        pageSize: 8,
      },
    },
  });

  return (
    <Stack gap="xl">
      <div>
        <Title order={1}>Investment Options</Title>

        <Text c="dimmed" mt={4}>
          Browse all available investment opportunities.
        </Text>
      </div>

      <SectionCard
        title="Investment Options"
        rightSection={
          <Group>
            <SearchBar
              value={search}
              onChange={setSearch}
              placeholder="Search investments..."
            />

            <Tooltip label="Filter by category">
              <ActionIcon
                variant="light"
                size="lg"
              >
                <IconAdjustmentsHorizontal size={18} />
              </ActionIcon>
            </Tooltip>

            <Tooltip label="Diversification Tool">
              <ActionIcon
                variant="light"
                color="violet"
                size="lg"
              >
                <IconDiamond size={18} />
              </ActionIcon>
            </Tooltip>
          </Group>
        }
      >
        {loading ? (
          <Text c="dimmed">Loading investment options...</Text>
        ) : filteredData.length === 0 ? (
          <Text c="dimmed">No investments found.</Text>
        ) : (
          <>
            <DataTable table={table} />
            <DataTablePagination table={table} />
          </>
        )}
      </SectionCard>
    </Stack>
  );
};

export default InvestmentOptions;