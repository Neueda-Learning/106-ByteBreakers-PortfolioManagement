const inrFormatter = new Intl.NumberFormat("en-IN", {
  style: "currency",
  currency: "INR",
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
});

export function formatCurrency(
  value: number | string | null | undefined,
): string {
  const numericValue = typeof value === "string" ? Number(value) : value;

  if (numericValue == null || Number.isNaN(numericValue)) {
    return inrFormatter.format(0);
  }

  return inrFormatter.format(numericValue);
}
