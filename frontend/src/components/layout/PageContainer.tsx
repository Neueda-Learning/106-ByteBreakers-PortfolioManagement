import { Box, Stack, Title } from "@mantine/core";
import type { ReactNode } from "react";

interface PageContainerProps {
  title: string;
  children: ReactNode;
}

const PageContainer = ({ title, children }: PageContainerProps) => {
  return (
    <Stack gap="xl" maw={1400} mx="auto">
      <Title order={1}>{title}</Title>

      <Box>{children}</Box>
    </Stack>
  );
};

export default PageContainer;
