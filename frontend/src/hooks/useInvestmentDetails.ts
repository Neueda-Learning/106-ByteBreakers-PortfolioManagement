import axios from "axios";
import { useEffect, useState } from "react";

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

  useEffect(() => {
    const fetchInvestmentDetails = async () => {
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
          `http://localhost:8081/investment-options/${id}/holdings`,
        );
        setInvestment(response.data ?? null);
      } catch {
        setInvestment(null);
        setError("Unable to load investment details.");
      } finally {
        setLoading(false);
      }
    };

    void fetchInvestmentDetails();
  }, [id]);

  return {
    investment,
    loading,
    error,
  };
}
