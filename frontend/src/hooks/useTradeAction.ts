import axios from "axios";
import { useState } from "react";
import { API_BASE_URL } from "../api/config";

export type TradeAction = "buy" | "sell";

interface ExecuteTradePayload {
  optionId: number;
  quantity: number;
  currentPrice: number;
  action: TradeAction;
}

export function useTradeAction() {
  const [submitting, setSubmitting] = useState(false);

  const executeTrade = async ({
    optionId,
    quantity,
    currentPrice,
    action,
  }: ExecuteTradePayload) => {
    setSubmitting(true);

    try {
      await axios.put(`${API_BASE_URL}/api/v1/${action}`, {
        optionId,
        quantity,
        currentPrice,
        action,
      });
    } finally {
      setSubmitting(false);
    }
  };

  return {
    executeTrade,
    submitting,
  };
}
