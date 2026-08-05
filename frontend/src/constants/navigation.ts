import {
  IconChartLine,
  IconLayoutDashboard,
  IconSearch,
  IconWallet,
} from "@tabler/icons-react";

import { ROUTES } from "./routes";

export const NAVIGATION_ITEMS = [
  {
    label: "Dashboard",
    icon: IconLayoutDashboard,
    path: ROUTES.DASHBOARD,
  },
  {
    label: "My Investments",
    icon: IconWallet,
    path: ROUTES.MYINVESTMENTS,
  },
  {
    label: "Investment Explorer",
    icon: IconSearch,
    path: ROUTES.EXPLORER,
  },
  {
    label: "Market Simulation",
    icon: IconChartLine,
    path: ROUTES.SIMULATION,
  },
] as const;
