import axios from "axios";
import { useEffect, useState } from "react";

export interface RecentInvestment {
  name: string;
  category: string;
  action: string;
  quantity: number;
  price: number;
  date: string;
}

export interface PortfolioSummary {
  totalInvestment: number;
  currentValue: number;
  totalProfitLoss: number;
  totalProfitLossPct: number;
  allocationByCategory: Record<string, number>;
  recentInvestments: RecentInvestment[];
}

export function usePortfolioSummary() {
  const [summary, setSummary] = useState<PortfolioSummary | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const response = await axios.get<PortfolioSummary>(
          "http://localhost:8081/api/portfolio/summary",
        );
        setSummary(response.data ?? null);
      } catch {
        setSummary(null);
      } finally {
        setLoading(false);
      }
    };

    void fetchSummary();
  }, []);

  return {
    summary,
    loading,
  };
}
