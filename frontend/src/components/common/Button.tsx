import {
  Button as MantineButton,
  type ButtonProps as MantineButtonProps,
  type ElementProps,
} from "@mantine/core";

export interface ButtonProps
  extends
    MantineButtonProps,
    ElementProps<"button", keyof MantineButtonProps> {}

const Button = ({
  color,
  fw = 600,
  radius = "md",
  size = "sm",
  variant = "filled",
  ...props
}: ButtonProps) => {
  return (
    <MantineButton
      color={color ?? "brand"}
      fw={fw}
      radius={radius}
      size={size}
      variant={variant}
      {...props}
    />
  );
};

export default Button;
