import { AppShell, NavLink, Stack, Text, Title } from "@mantine/core";
import { NavLink as RouterNavLink } from "react-router-dom";

import { NAVIGATION_ITEMS } from "@/constants/navigation";

const Sidebar = () => {
    return (
        <AppShell.Navbar p="md">
            <Title order={3} mb="xl">
                Portfolio Manager
            </Title>

            <Stack gap="xs">
                {NAVIGATION_ITEMS.map((item) => (
                    <NavLink
                        key={item.path}
                        component={RouterNavLink}
                        to={item.path}
                        label={item.label}
                        leftSection={<item.icon size={18} />}
                    />
                ))}
            </Stack>

            <Text c="dimmed" size="xs" mt="auto">
                Spring Boot Training Project
            </Text>
        </AppShell.Navbar>
    );
};

export default Sidebar;