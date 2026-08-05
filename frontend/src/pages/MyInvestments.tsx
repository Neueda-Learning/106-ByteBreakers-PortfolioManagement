import { useMemo, useState } from "react";

import { Button, Select, SimpleGrid, Text } from "@mantine/core";

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
  Toolbar,
  SearchBar,
  SectionCard,
  StatCard,
  StatusBadge,
} from "@/components/common";

import { PageContainer } from "@/components/layout";
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

const Investments = () => {
  const [search, setSearch] = useState("");
  const { investments, loading } = useMyInvestments();

  const portfolioValue = useMemo(
    () =>
      investments.reduce(
        (total, investment) =>
          total +
          toNumber(investment.currentPrice) *
            toNumber(investment.totalQuantityOwned),
        0,
      ),
    [investments],
  );

  const quantityOwned = useMemo(
    () =>
      investments.reduce(
        (total, investment) => total + toNumber(investment.totalQuantityOwned),
        0,
      ),
    [investments],
  );

  const trendingUpCount = useMemo(
    () =>
      investments.filter((investment) =>
        String(investment.trend || "")
          .toLowerCase()
          .includes("up"),
      ).length,
    [investments],
  );

  const filteredData = useMemo(() => {
    return investments.filter((investment) =>
      `${investment.name} ${investment.category}`
        .toLowerCase()
        .includes(search.toLowerCase()),
    );
  }, [investments, search]);

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
        header: "Current Value",
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
        cell: () => (
          <Button variant="light" color="brand" size="xs">
            View Details
          </Button>
        ),
      },
    ],
    [],
  );

  const table = useReactTable({
    data: filteredData,
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
      <SimpleGrid cols={3} spacing="md" mb="lg">
        <StatCard
          title="Portfolio Value"
          value={formatCurrency(portfolioValue)}
        />

        <StatCard
          title="Holdings"
          value={`${investments.length} Investments`}
        />

        <StatCard
          title="Total Units"
          value={quantityOwned.toLocaleString("en-IN")}
        />

        <StatCard
          title="Trending Up"
          value={`${trendingUpCount} Assets`}
          trend="positive"
        />
      </SimpleGrid>

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
                  placeholder="Category"
                  data={["All", "Equity", "Mutual Fund", "Crypto", "Bond"]}
                  w={180}
                  variant="filled"
                  color="brand"
                  radius="md"
                />
              </>
            }
            rightSection={
              <Select
                placeholder="Sort"
                data={["Name", "Profit/Loss", "Current Value"]}
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
        ) : filteredData.length === 0 ? (
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
