export interface User {
  id: string
  name: string
  email: string
  phoneNumber?: string
  title?: string
  role: 'USER' | 'ADMIN'
  verified: boolean
}

export interface Company {
  id: string
  name: string
  missionStatement: string
  address: string
  website: string
}

export interface SignatureTemplate {
  htmlContent: string
  plainTextContent: string
  signatureHtml: string
}

export interface AuthResponse {
  token: string
  email: string
  message: string
}

export interface ApiError {
  message: string
  status: number
  errors?: Record<string, string>
}

export interface ApiError {
  response?: {
    data?: {
      message?: string
      validationErrors?: Record<string, string>
    }
    status?: number
  }
  message: string
}
