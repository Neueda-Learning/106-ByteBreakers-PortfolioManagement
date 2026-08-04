import { AppShell } from "@mantine/core";
import { Outlet } from "react-router-dom";

import Sidebar from "./Sidebar";

const Layout = () => {
    return (
        <AppShell
            navbar={{
                width: 260,
                breakpoint: "sm",
            }}
            padding="md"
        >
            <Sidebar />

            <AppShell.Main>
                <Outlet />
            </AppShell.Main>
        </AppShell>
    );
};

export default Layout;