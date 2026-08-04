import { createTheme } from "@mantine/core";
import { BRAND_COLORS } from "./colours";

export const theme = createTheme({
  primaryColor: "brand",
  primaryShade: 6,

  colors: {
    brand: BRAND_COLORS,
  },

  defaultRadius: "md",
  fontFamily: "Inter, sans-serif",
});