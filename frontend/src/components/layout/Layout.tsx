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
            styles={(theme) => ({
                main: {
                    backgroundColor: theme.colors.brand[0],
                },
            })}
        >
            <Sidebar />

            <AppShell.Main>
                <Outlet />
            </AppShell.Main>
        </AppShell>
    );
};

export default Layout;