import { Stack, Text, Title } from "@mantine/core";

import { IconClock } from "@tabler/icons-react";

import { SectionCard } from "@/components/common";
import { PageContainer } from "@/components/layout";

const MarketSimulation = () => {
  return (
    <PageContainer title="Market Simulation">
      <SectionCard>
        <Stack align="center" justify="center" gap="md" py="xl">
          <IconClock size={64} stroke={1.2} color="gray" />

          <Title order={2} c="dimmed">
            Coming Soon
          </Title>

          <Text c="dimmed" ta="center" maw={400}>
            Market Simulation is under development. Check back later.
          </Text>
        </Stack>
      </SectionCard>
    </PageContainer>
  );
};

export default MarketSimulation;
