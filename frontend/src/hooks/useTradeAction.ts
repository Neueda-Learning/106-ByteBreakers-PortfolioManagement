import axios from "axios";
import { useState } from "react";

export type TradeAction = "buy" | "sell";

interface ExecuteTradePayload {
  optionId: number;
  quantity: number;
  currentPrice: number;
  action: TradeAction;
}

const API_BASE_URL = "http://localhost:8081/api/v1";

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
      await axios.put(`${API_BASE_URL}/${action}`, {
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
