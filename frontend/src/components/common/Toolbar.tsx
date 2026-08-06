import { Group } from "@mantine/core";
import type { ReactNode } from "react";

interface ToolbarProps {
    leftSection?: ReactNode;
    rightSection?: ReactNode;
}

const Toolbar = ({
    leftSection,
    rightSection,
}: ToolbarProps) => {
    return (
        <Group
            justify="space-between"
            mb="lg"
            wrap="wrap"
        >
            <Group>
                {leftSection}
            </Group>

            <Group>
                {rightSection}
            </Group>
        </Group>
    );
};

export default Toolbar;