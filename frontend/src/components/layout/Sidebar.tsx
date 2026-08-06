import { AppShell, NavLink, Stack, Title } from "@mantine/core";
import { NavLink as RouterNavLink } from "react-router-dom";

import { NAVIGATION_ITEMS } from "@/constants/navigation";

const Sidebar = () => {
  return (
    <AppShell.Navbar
      p="md"
      style={{
        // backgroundColor: "var(--mantine-color-brand-1)",
        borderRight: "1px solid var(--mantine-color-brand-2)",
      }}
    >
      <Title
        order={3}
        mb="xl"
        // c="brand.9"
      >
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
    </AppShell.Navbar>
  );
};

export default Sidebar;
