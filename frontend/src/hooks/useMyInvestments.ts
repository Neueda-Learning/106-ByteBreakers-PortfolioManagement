import axios from "axios";
import { useEffect, useState } from "react";
import { API_BASE_URL } from "../api/config";

export interface MyInvestment {
  name: string;
  category: string;
  currentPrice: number;
  totalQuantityOwned: number;
  trend: string;
}

export function useMyInvestments() {
  const [investments, setInvestments] = useState<MyInvestment[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMyInvestments = async () => {
      try {
        const response = await axios.get<MyInvestment[]>(
          `${API_BASE_URL}/api/v1/investment`,
        );
        setInvestments(Array.isArray(response.data) ? response.data : []);
      } catch {
        setInvestments([]);
      } finally {
        setLoading(false);
      }
    };

    void fetchMyInvestments();
  }, []);

  return {
    investments,
    loading,
  };
}
