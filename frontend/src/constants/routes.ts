export const ROUTES = {
  DASHBOARD: "/",
  INVESTMENTOPTIONS: "/investment_options",
  MYINVESTMENTS: "/my_investments",
  DIVERSIFYRECOMMENDATIONS: "/diversify_recommendations",
  INVESTMENTDETAILS: "/investment_details/:id",
  EXPLORER: "/explorer",
  SIMULATION: "/simulation",
} as const;

export const getInvestmentDetailsPath = (id: number | string) =>
  `/investment_details/${id}`;
