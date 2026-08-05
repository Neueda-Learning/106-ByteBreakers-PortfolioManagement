import { Card, Group, Stack, Text, ThemeIcon, Title } from "@mantine/core";

import { IconTrendingDown, IconTrendingUp } from "@tabler/icons-react";

import type { ReactNode } from "react";

interface StatCardProps {
  title: string;
  value: ReactNode;

  change?: string;

  trend?: "positive" | "negative" | "neutral";

  icon?: ReactNode;
}

const StatCard = ({
  title,
  value,
  change,
  trend = "neutral",
  icon,
}: StatCardProps) => {
  const getTrendColor = () => {
    switch (trend) {
      case "positive":
        return "green";

      case "negative":
        return "red";

      default:
        return "gray";
    }
  };

  return (
    <Card
      withBorder
      radius="lg"
      shadow="sm"
      p="lg"
      sx={(theme) => ({
        backgroundColor: theme.colors.brand[0],
        borderColor: theme.colors.brand[2],
      })}
    >
      <Stack gap="xs">
        <Group justify="space-between">
          <Text fw={500} c="dimmed">
            {title}
          </Text>

          {icon && (
            <ThemeIcon variant="light" color="brand">
              {icon}
            </ThemeIcon>
          )}
        </Group>

        <Title order={2}>{value}</Title>

        {change && (
          <Group gap={4}>
            {trend === "positive" && <IconTrendingUp size={16} color="green" />}

            {trend === "negative" && <IconTrendingDown size={16} color="red" />}

            <Text size="sm" c={getTrendColor()} fw={600}>
              {change}
            </Text>
          </Group>
        )}
      </Stack>
    </Card>
  );
};

export default StatCard;
