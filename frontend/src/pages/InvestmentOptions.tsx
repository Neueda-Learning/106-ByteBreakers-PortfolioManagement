import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { Group, Select, Stack, Text, Title } from "@mantine/core";

import {
  type ColumnDef,
  getCoreRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useReactTable,
} from "@tanstack/react-table";

import {
  Button,
  DataTable,
  DataTablePagination,
  SearchBar,
  SectionCard,
  showToast,
  StatusBadge,
  TradeQuantityModal,
  Toolbar,
} from "@/components/common";
import { getInvestmentDetailsPath } from "@/constants/routes";
import { useInvestments } from "@/hooks/useInvestments";
import { useTradeAction } from "@/hooks/useTradeAction";

interface InvestmentOption {
  id: number;
  name: string;
  category: string;
  currentPrice: number;
  trend: string;
  estimatedReturn: number;
}

const formatCurrency = (value: number) => `₹${value.toLocaleString("en-IN")}`;

const InvestmentOptions = () => {
  const navigate = useNavigate();
  const [category, setCategory] = useState<string | null>("All");
  const [search, setSearch] = useState("");
  const [sortBy, setSortBy] = useState<string | null>("Name");
  const [selectedInvestment, setSelectedInvestment] =
    useState<InvestmentOption | null>(null);
  const { investments, loading } = useInvestments();
  const { executeTrade, submitting } = useTradeAction();

  const handleConfirmBuy = async (quantity: number) => {
    if (!selectedInvestment) {
      return;
    }

    const investmentToBuy = selectedInvestment;

    try {
      await executeTrade({
        optionId: investmentToBuy.id,
        quantity,
        currentPrice: investmentToBuy.currentPrice,
        action: "buy",
      });

      showToast({
        title: "Buy order submitted",
        message: `Bought ${quantity} of ${investmentToBuy.name}.`,
        tone: "success",
      });

      setSelectedInvestment(null);
      navigate(getInvestmentDetailsPath(investmentToBuy.id));
    } catch {
      showToast({
        title: "Unable to place order",
        message: "Please try again.",
        tone: "error",
      });
    }
  };

  const categoryOptions = useMemo(() => {
    const categories = Array.from(
      new Set(investments.map((investment) => investment.category)),
    ).sort((left, right) => left.localeCompare(right));

    return ["All", ...categories];
  }, [investments]);

  const visibleData = useMemo(() => {
    const normalizedSearch = search.toLowerCase();

    const filteredInvestments = investments.filter((investment) => {
      const matchesSearch = `${investment.name} ${investment.category}`
        .toLowerCase()
        .includes(normalizedSearch);

      const matchesCategory =
        !category || category === "All" || investment.category === category;

      return matchesSearch && matchesCategory;
    });

    return [...filteredInvestments].sort((left, right) => {
      switch (sortBy) {
        case "Current Price":
          return right.currentPrice - left.currentPrice;
        case "Est. Return":
          return right.estimatedReturn - left.estimatedReturn;
        case "Category":
          return left.category.localeCompare(right.category);
        case "Name":
        default:
          return left.name.localeCompare(right.name);
      }
    });
  }, [category, investments, search, sortBy]);

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
          <StatusBadge color="blue">{row.original.category}</StatusBadge>
        ),
      },
      {
        accessorKey: "currentPrice",
        header: "Current Price",
        cell: ({ row }) => formatCurrency(row.original.currentPrice),
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
        cell: ({ row }) => (
          <Group gap="xs" wrap="nowrap">
            {/* <Button
              size="sm"
              variant="light"
              leftSection={<IconEye size={14} />}
              onClick={() =>
                navigate(getInvestmentDetailsPath(row.original.id))
              }
            >
              View Details
            </Button> */}

            <Button
              color="green"
              size="sm"
              onClick={() => setSelectedInvestment(row.original)}
            >
              Buy
            </Button>
          </Group>
        ),
      },
    ],
    [],
  );

  const table = useReactTable({
    data: visibleData,
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
          <Toolbar
            leftSection={
              <>
                <SearchBar
                  value={search}
                  onChange={setSearch}
                  placeholder="Search investments..."
                />

                <Select
                  value={category}
                  onChange={setCategory}
                  placeholder="Category"
                  data={categoryOptions}
                  w={180}
                  variant="filled"
                  color="brand"
                  radius="md"
                />
              </>
            }
            rightSection={
              <Select
                value={sortBy}
                onChange={setSortBy}
                placeholder="Sort"
                data={["Name", "Current Price", "Est. Return", "Category"]}
                w={180}
                variant="filled"
                color="brand"
                radius="md"
              />
            }
          />
        }
      >
        {loading ? (
          <Text c="dimmed">Loading investment options...</Text>
        ) : visibleData.length === 0 ? (
          <Text c="dimmed">No investments found.</Text>
        ) : (
          <>
            <DataTable table={table} />
            <DataTablePagination table={table} />
          </>
        )}
      </SectionCard>

      <TradeQuantityModal
        opened={selectedInvestment != null}
        action="buy"
        investmentName={selectedInvestment?.name ?? "Investment"}
        currentPrice={selectedInvestment?.currentPrice ?? 0}
        submitting={submitting}
        onClose={() => setSelectedInvestment(null)}
        onConfirm={(quantity) => void handleConfirmBuy(quantity)}
      />
    </Stack>
  );
};

export default InvestmentOptions;
