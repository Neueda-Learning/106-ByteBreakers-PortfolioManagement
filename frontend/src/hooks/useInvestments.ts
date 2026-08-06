import axios from "axios";
import { useEffect, useState } from "react";
import { API_BASE_URL } from "../api/config";

interface InvestmentOption {
  id: number;
  name: string;
  category: string;
  currentPrice: number;
  trend: string;
  estimatedReturn: number;
  [key: string]: any;
}

export function useInvestments() {
  const [investments, setInvestments] = useState<InvestmentOption[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchInvestments = async () => {
      try {
        const response = await axios.get<InvestmentOption[]>(
          `${API_BASE_URL}/investment-options/`,
        );
        setInvestments(Array.isArray(response.data) ? response.data : []);
      } catch {
        setInvestments([]);
      } finally {
        setLoading(false);
      }
    };

    void fetchInvestments();
  }, []);

  return {
    investments,
    loading,
  };
}
