import { Group, Pagination, Text } from "@mantine/core";

import { type Table as TanStackTable } from "@tanstack/react-table";

interface DataTablePaginationProps<T> {
    table: TanStackTable<T>;
}

const DataTablePagination = <T,>({
    table,
}: DataTablePaginationProps<T>) => {
    return (
        <Group
            justify="space-between"
            mt="lg"
        >
            <Text
                size="sm"
                c="dimmed"
            >
                Showing{" "}
                {table.getRowModel().rows.length} of{" "}
                {table.getCoreRowModel().rows.length} entries
            </Text>

            <Pagination
                total={table.getPageCount()}
                value={table.getState().pagination.pageIndex + 1}
                onChange={(page) =>
                    table.setPageIndex(page - 1)
                }
            />
        </Group>
    );
};

export default DataTablePagination;