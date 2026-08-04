import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import { MantineProvider } from "@mantine/core";
import { Notifications } from "@mantine/notifications";

import "@mantine/core/styles.css";
import "@mantine/notifications/styles.css";
import "@fontsource/inter";

import App from "./App";
import { theme } from "@/theme/theme";

createRoot(document.getElementById("app")!).render(
  <StrictMode>
    <MantineProvider theme={theme}>
      <Notifications />
      <App />
    </MantineProvider>
  </StrictMode>
);