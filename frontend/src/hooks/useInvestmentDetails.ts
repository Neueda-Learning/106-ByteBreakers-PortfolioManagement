import axios from "axios";
import { useCallback, useEffect, useState } from "react";
import { API_BASE_URL } from "../api/config";

export interface InvestmentDetail {
  id: number;
  name: string;
  category: string;
  currentPrice: number;
  trend: string;
  estimatedReturn: number;
  quantityOwned: number;
  totalInvested: number;
  currentProfitLoss: number;
}

export function useInvestmentDetails(id?: number) {
  const [investment, setInvestment] = useState<InvestmentDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchInvestmentDetails = useCallback(async () => {
    if (!id || Number.isNaN(id)) {
      setInvestment(null);
      setError("Invalid investment id.");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await axios.get<InvestmentDetail>(
        `${API_BASE_URL}/investment-options/${id}/holdings`,
      );
      setInvestment(response.data ?? null);
    } catch {
      setInvestment(null);
      setError("Unable to load investment details.");
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    void fetchInvestmentDetails();
  }, [fetchInvestmentDetails]);

  return {
    investment,
    loading,
    error,
    refetch: fetchInvestmentDetails,
  };
}
