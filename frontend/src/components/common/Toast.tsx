import { Group, Stack, Text, ThemeIcon } from "@mantine/core";
import { notifications } from "@mantine/notifications";
import {
  IconAlertCircle,
  IconCheck,
  IconInfoCircle,
  IconAlertTriangle,
} from "@tabler/icons-react";
import type { ReactNode } from "react";

type ToastTone = "success" | "error" | "info" | "warning";

interface ToastProps {
  title: ReactNode;
  message?: ReactNode;
  tone?: ToastTone;
}

interface ShowToastOptions extends ToastProps {
  autoClose?: number | false;
}

const toneConfig: Record<
  ToastTone,
  {
    color: string;
    icon: typeof IconCheck;
  }
> = {
  success: {
    color: "green",
    icon: IconCheck,
  },
  error: {
    color: "red",
    icon: IconAlertCircle,
  },
  info: {
    color: "brand",
    icon: IconInfoCircle,
  },
  warning: {
    color: "yellow",
    icon: IconAlertTriangle,
  },
};

const Toast = ({ title, message, tone = "info" }: ToastProps) => {
  const config = toneConfig[tone];
  const Icon = config.icon;

  return (
    <Group align="flex-start" gap="sm" wrap="nowrap">
      <ThemeIcon color={config.color} radius="xl" size="lg" variant="light">
        <Icon size={16} />
      </ThemeIcon>

      <Stack gap={2}>
        <Text fw={600} size="sm">
          {title}
        </Text>

        {message ? (
          <Text c="dimmed" size="sm">
            {message}
          </Text>
        ) : null}
      </Stack>
    </Group>
  );
};

export const showToast = ({
  title,
  message,
  tone = "info",
  autoClose = 4000,
}: ShowToastOptions) => {
  const config = toneConfig[tone];
  const Icon = config.icon;

  notifications.show({
    title,
    message,
    autoClose,
    color: config.color,
    icon: <Icon size={16} />,
    radius: "md",
    withBorder: true,
  });
};

export default Toast;
