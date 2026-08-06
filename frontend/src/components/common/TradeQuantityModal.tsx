import { useEffect, useMemo, useState } from "react";

import { Group, Modal, NumberInput, Stack, Text } from "@mantine/core";

import Button from "./Button";

import { formatCurrency } from "@/util/currency";

import type { TradeAction } from "@/hooks/useTradeAction";

interface TradeQuantityModalProps {
  opened: boolean;
  action: TradeAction;
  investmentName: string;
  currentPrice: number;
  maxQuantity?: number;
  submitting?: boolean;
  onClose: () => void;
  onConfirm: (quantity: number) => void | Promise<void>;
}

const TradeQuantityModal = ({
  opened,
  action,
  investmentName,
  currentPrice,
  maxQuantity,
  submitting = false,
  onClose,
  onConfirm,
}: TradeQuantityModalProps) => {
  const [quantity, setQuantity] = useState<number | "">("");

  useEffect(() => {
    if (opened) {
      setQuantity("");
    }
  }, [opened]);

  const numericQuantity = typeof quantity === "number" ? quantity : 0;

  const estimatedValue = useMemo(
    () => numericQuantity * currentPrice,
    [numericQuantity, currentPrice],
  );

  const actionLabel = action === "buy" ? "Buy" : "Sell";
  const actionColor = action === "buy" ? "green" : "red";
  const exceedsMaxQuantity =
    typeof maxQuantity === "number" && numericQuantity > maxQuantity;
  const isInvalidQuantity = numericQuantity <= 0 || exceedsMaxQuantity;

  const handleConfirm = async () => {
    if (numericQuantity <= 0) {
      return;
    }

    if (exceedsMaxQuantity) {
      return;
    }

    await onConfirm(numericQuantity);
  };

  const handleQuantityChange = (value: number | string) => {
    setQuantity(
      value === "" ? "" : typeof value === "string" ? parseFloat(value) : value,
    );
  };

  return (
    <Modal
      opened={opened}
      onClose={onClose}
      title={`${actionLabel} ${investmentName}`}
      centered
      radius="md"
    >
      <Stack gap="md">
        <Text size="sm" c="dimmed">
          Current price: {formatCurrency(currentPrice)}
        </Text>

        <NumberInput
          label="Quantity"
          placeholder="Enter quantity"
          min={0}
          step={1}
          decimalScale={2}
          value={quantity}
          onChange={handleQuantityChange}
          max={maxQuantity}
          allowNegative={false}
          clampBehavior="strict"
        />

        {typeof maxQuantity === "number" && action === "sell" ? (
          <Text size="sm" c={exceedsMaxQuantity ? "red" : "dimmed"}>
            Quantity owned: {maxQuantity.toLocaleString("en-IN")}
          </Text>
        ) : null}

        <Text size="sm" c="dimmed">
          Estimated {action === "buy" ? "cost" : "proceeds"}:{" "}
          {formatCurrency(estimatedValue)}
        </Text>

        <Group justify="flex-end" mt="sm">
          <Button variant="default" onClick={onClose} disabled={submitting}>
            Cancel
          </Button>

          <Button
            color={actionColor}
            onClick={() => void handleConfirm()}
            loading={submitting}
            disabled={isInvalidQuantity}
          >
            Confirm {actionLabel}
          </Button>
        </Group>
      </Stack>
    </Modal>
  );
};

export default TradeQuantityModal;
