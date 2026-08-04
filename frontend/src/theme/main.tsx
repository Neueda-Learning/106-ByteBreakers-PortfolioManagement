import "@mantine/core/styles.css";
import "@mantine/notifications/styles.css";
import "@fontsource/inter";

import { MantineProvider } from "@mantine/core";
import { Notifications } from "@mantine/notifications";

import { theme } from "./theme";

import "./styles/globals.css";
import App from "../App";

<MantineProvider theme={theme}>
    <Notifications />
    <App />
</MantineProvider>