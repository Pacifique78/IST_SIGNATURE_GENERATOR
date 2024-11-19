import type { ApiError } from "@/types";

export const handleApiError = (error: unknown): string => {
    if (error && typeof error === 'object' && 'response' in error) {
      const apiError = error as ApiError;
      return apiError.response?.data?.message || 'An error occurred';
    }
    return 'An unexpected error occurred';
  };