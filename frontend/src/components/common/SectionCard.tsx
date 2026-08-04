import { Card, Group, Stack, Text, Title } from "@mantine/core";
import type { ReactNode } from "react";

interface SectionCardProps {
    children: ReactNode;
    title?: string;
    subtitle?: string;
    rightSection?: ReactNode;
}

const SectionCard = ({
    children,
    title,
    subtitle,
    rightSection,
}: SectionCardProps) => {
    return (
        <Card
            shadow="sm"
            radius="lg"
            withBorder
            p="lg"
        >
            {(title || rightSection) && (
                <Group
                    justify="space-between"
                    align="flex-start"
                    mb="lg"
                >
                    <Stack gap={2}>
                        {title && (
                            <Title order={4}>
                                {title}
                            </Title>
                        )}

                        {subtitle && (
                            <Text
                                size="sm"
                                c="dimmed"
                            >
                                {subtitle}
                            </Text>
                        )}
                    </Stack>

                    {rightSection}
                </Group>
            )}

            {children}
        </Card>
    );
};

export default SectionCard;