import { Badge } from "@mantine/core";
import type { ReactNode } from "react";

interface StatusBadgeProps {
    children: ReactNode;
    color: string;
}

const StatusBadge = ({ children, color }: StatusBadgeProps) => {
    return (
        <Badge
            color={color}
            variant="light"
            radius="sm"
        >
            {children}
        </Badge>
    );
};

export default StatusBadge;