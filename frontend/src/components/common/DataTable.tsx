import {
    flexRender,
    type Table as TanStackTable,
} from "@tanstack/react-table";

import { Table } from "@mantine/core";

interface DataTableProps<T> {
    table: TanStackTable<T>;
}

const DataTable = <T,>({
    table,
}: DataTableProps<T>) => {
    return (
        <Table
            highlightOnHover
            withTableBorder
            withColumnBorders={false}
            stickyHeader
            horizontalSpacing="md"
            verticalSpacing="sm"
        >
            <Table.Thead>
                {table.getHeaderGroups().map((headerGroup) => (
                    <Table.Tr key={headerGroup.id}>
                        {headerGroup.headers.map((header) => (
                            <Table.Th key={header.id}>
                                {header.isPlaceholder
                                    ? null
                                    : flexRender(
                                        header.column.columnDef.header,
                                        header.getContext()
                                    )}
                            </Table.Th>
                        ))}
                    </Table.Tr>
                ))}
            </Table.Thead>

            <Table.Tbody>
                {table.getRowModel().rows.map((row) => (
                    <Table.Tr key={row.id}>
                        {row.getVisibleCells().map((cell) => (
                            <Table.Td key={cell.id}>
                                {flexRender(
                                    cell.column.columnDef.cell,
                                    cell.getContext()
                                )}
                            </Table.Td>
                        ))}
                    </Table.Tr>
                ))}
            </Table.Tbody>
        </Table>
    );
};

export default DataTable;