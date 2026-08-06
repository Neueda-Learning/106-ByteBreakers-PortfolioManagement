import { flexRender, type Table as TanStackTable } from "@tanstack/react-table";

import { Table } from "@mantine/core";

declare module "@tanstack/react-table" {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  interface ColumnMeta<TData, TValue> {
    align?: "left" | "center" | "right";
  }
}

interface DataTableProps<T> {
  table: TanStackTable<T>;
}

const DataTable = <T,>({ table }: DataTableProps<T>) => {
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
              <Table.Th
                key={header.id}
                style={{
                  textAlign: header.column.columnDef.meta?.align ?? "left",
                  paddingRight:
                    header.column.columnDef.meta?.align === "right"
                      ? "8rem"
                      : undefined,
                }}
              >
                {header.isPlaceholder
                  ? null
                  : flexRender(
                      header.column.columnDef.header,
                      header.getContext(),
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
              <Table.Td
                key={cell.id}
                style={{
                  textAlign: cell.column.columnDef.meta?.align ?? "left",
                  paddingRight:
                    cell.column.columnDef.meta?.align === "right"
                      ? "8rem"
                      : undefined,
                }}
              >
                {flexRender(cell.column.columnDef.cell, cell.getContext())}
              </Table.Td>
            ))}
          </Table.Tr>
        ))}
      </Table.Tbody>
    </Table>
  );
};

export default DataTable;
