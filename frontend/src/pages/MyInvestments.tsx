import { useMemo, useState } from "react";

import {
    Button,
    Select,
    SimpleGrid,
} from "@mantine/core";

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

interface Investment {
    id: number;
    name: string;
    category: string;
    quantity: number;
    averageBuyPrice: number;
    currentPrice: number;
}

const investments: Investment[] = [
    {
        id: 1,
        name: "Tata Motors",
        category: "Equity",
        quantity: 20,
        averageBuyPrice: 720,
        currentPrice: 755,
    },
    {
        id: 2,
        name: "HDFC Flexi Cap Fund",
        category: "Mutual Fund",
        quantity: 45,
        averageBuyPrice: 82,
        currentPrice: 88,
    },
    {
        id: 3,
        name: "Bitcoin",
        category: "Crypto",
        quantity: 0.05,
        averageBuyPrice: 5400000,
        currentPrice: 5900000,
    },
    {
        id: 4,
        name: "SGB 2033",
        category: "Bond",
        quantity: 10,
        averageBuyPrice: 7100,
        currentPrice: 7350,
    },
];

const formatCurrency = (value: number) =>
    `₹${value.toLocaleString("en-IN")}`;

const Investments = () => {
    const [search, setSearch] = useState("");

    const filteredData = useMemo(() => {
        return investments.filter((investment) =>
            investment.name
                .toLowerCase()
                .includes(search.toLowerCase())
        );
    }, [search]);

    const columns = useMemo<ColumnDef<Investment>[]>(
        () => [
            {
                accessorKey: "name",
                header: "Investment",
            },
            {
                accessorKey: "category",
                header: "Category",
                cell: ({ row }) => (
                    <StatusBadge color="brand">
                        {row.original.category}
                    </StatusBadge>
                ),
            },
            {
                accessorKey: "quantity",
                header: "Quantity",
            },
            {
                header: "Avg. Buy",
                cell: ({ row }) =>
                    formatCurrency(row.original.averageBuyPrice),
            },
            {
                id: "currentPrice",
                header: "Current Price",
                cell: ({ row }) =>
                    formatCurrency(row.original.currentPrice),
            },
            {
                id: "currentValue",
                header: "Current Value",
                cell: ({ row }) =>
                    formatCurrency(
                        row.original.currentPrice *
                        row.original.quantity
                    ),
            },
            {
                id: "profitLoss",
                header: "P/L",
                cell: ({ row }) => {
                    const profit =
                        (row.original.currentPrice -
                            row.original.averageBuyPrice) *
                        row.original.quantity;

                    const positive = profit >= 0;

                    return (
                        <span
                            style={{
                                color: positive
                                    ? "green"
                                    : "red",
                                fontWeight: 600,
                            }}
                        >
                            {positive ? "+" : ""}
                            {formatCurrency(profit)}
                        </span>
                    );
                },
            },
            {
                id: "actions",
                header: "",
                cell: () => (
                    <Button
                        variant="light"
                        color="brand"
                        size="xs"
                    >
                        View Details
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
        getPaginationRowModel:
            getPaginationRowModel(),

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
                    value="₹7,24,300"
                />

                <StatCard
                    title="Holdings"
                    value="4 Investments"
                />

                <StatCard
                    title="Unrealized P/L"
                    value="+₹34,500"
                    trend="positive"
                />
            </SimpleGrid>

            <SectionCard title="My Investments"
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
                                    data={[
                                        "All",
                                        "Equity",
                                        "Mutual Fund",
                                        "Crypto",
                                        "Bond",
                                    ]}
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
                                data={[
                                    "Name",
                                    "Profit/Loss",
                                    "Current Value",
                                ]}
                                w={180}
                                variant="filled"
                                color="brand"
                                radius="md"
                            />
                        }
                    />
                }>

                <DataTable table={table} />

                <DataTablePagination table={table} />

            </SectionCard>

        </PageContainer>
    );
};

export default Investments;