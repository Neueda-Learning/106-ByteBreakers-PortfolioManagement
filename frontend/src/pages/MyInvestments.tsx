import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { Select, Text } from "@mantine/core";

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
  Toolbar,
  SearchBar,
  SectionCard,
  StatusBadge,
} from "@/components/common";

import { PageContainer } from "@/components/layout";
import { getInvestmentDetailsPath } from "@/constants/routes";
import { useInvestments } from "@/hooks/useInvestments";
import { type MyInvestment, useMyInvestments } from "@/hooks/useMyInvestments";

const formatCurrency = (value: number) => `₹${value.toLocaleString("en-IN")}`;

const toNumber = (value: number | string) => Number(value);

const trendColor = (trendValue: string) => {
  const trend = String(trendValue || "").toLowerCase();

  if (trend.includes("up")) {
    return "green";
  }

  if (trend.includes("down")) {
    return "red";
  }

  return "gray";
};

const investmentLookupKey = (name: string, category: string) =>
  `${String(name || "").toLowerCase()}::${String(category || "").toLowerCase()}`;

const Investments = () => {
  const [category, setCategory] = useState<string | null>("All");
  const [search, setSearch] = useState("");
  const [sortBy, setSortBy] = useState<string | null>("Name");
  const navigate = useNavigate();
  const { investments, loading } = useMyInvestments();
  const { investments: investmentOptions } = useInvestments();

  const investmentIdByLookupKey = useMemo(() => {
    const map = new Map<string, number>();

    investmentOptions.forEach((option) => {
      map.set(investmentLookupKey(option.name, option.category), option.id);
    });

    return map;
  }, [investmentOptions]);

  const categoryOptions = useMemo(
    () => [
      "All",
      ...Array.from(
        new Set(investments.map((investment) => investment.category)),
      ).sort((left, right) => left.localeCompare(right)),
    ],
    [investments],
  );

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
        case "Current Value":
          return (
            toNumber(right.currentPrice) * toNumber(right.totalQuantityOwned) -
            toNumber(left.currentPrice) * toNumber(left.totalQuantityOwned)
          );
        case "Current Price":
          return toNumber(right.currentPrice) - toNumber(left.currentPrice);
        case "Quantity":
          return (
            toNumber(right.totalQuantityOwned) -
            toNumber(left.totalQuantityOwned)
          );
        case "Name":
        default:
          return left.name.localeCompare(right.name);
      }
    });
  }, [category, investments, search, sortBy]);

  const columns = useMemo<ColumnDef<MyInvestment>[]>(
    () => [
      {
        accessorKey: "name",
        header: "Investment",
      },
      {
        accessorKey: "category",
        header: "Category",
        cell: ({ row }) => (
          <StatusBadge color="brand">{row.original.category}</StatusBadge>
        ),
      },
      {
        accessorKey: "totalQuantityOwned",
        header: "Quantity",
        cell: ({ row }) =>
          toNumber(row.original.totalQuantityOwned).toLocaleString("en-IN"),
      },
      {
        id: "currentPrice",
        header: "Current Price",
        cell: ({ row }) => formatCurrency(toNumber(row.original.currentPrice)),
      },
      {
        id: "currentValue",
        header: "Holdings",
        cell: ({ row }) =>
          formatCurrency(
            toNumber(row.original.currentPrice) *
              toNumber(row.original.totalQuantityOwned),
          ),
      },
      {
        accessorKey: "trend",
        header: "Trend",
        cell: ({ row }) => {
          const trend = String(row.original.trend || "").toLowerCase();
          const normalizedTrend = trend.replaceAll("_", " ");

          return (
            <StatusBadge color={trendColor(trend)}>
              {normalizedTrend.toUpperCase()}
            </StatusBadge>
          );
        },
      },
      {
        id: "actions",
        header: "",
        cell: ({ row }) => {
          const optionId = investmentIdByLookupKey.get(
            investmentLookupKey(row.original.name, row.original.category),
          );

          return (
            <Button
              variant="light"
              color="brand"
              size="sm"
              disabled={!optionId}
              onClick={() => {
                if (!optionId) {
                  return;
                }

                navigate(getInvestmentDetailsPath(optionId));
              }}
            >
              View Details
            </Button>
          );
        },
      },
    ],
    [investmentIdByLookupKey, navigate],
  );

  const table = useReactTable({
    data: visibleData,
    columns,

    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getPaginationRowModel: getPaginationRowModel(),

    initialState: {
      pagination: {
        pageSize: 5,
      },
    },
  });

  return (
    <PageContainer title="My Investments">
      <SectionCard
        title="My Investments"
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
                data={["Name", "Current Value", "Current Price", "Quantity"]}
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
          <Text c="dimmed">Loading investments...</Text>
        ) : visibleData.length === 0 ? (
          <Text c="dimmed">No investments found.</Text>
        ) : (
          <>
            <DataTable table={table} />

            <DataTablePagination table={table} />
          </>
        )}
      </SectionCard>
    </PageContainer>
  );
};

export default Investments;
