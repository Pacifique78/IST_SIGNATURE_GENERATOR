export const emailValidator = (email: string): boolean => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return regex.test(email)
}

export const passwordValidator = (password: string): boolean => {
  return password.length >= 8
}

export const phoneValidator = (phone: string): boolean => {
  const regex = /^\+?[1-9]\d{1,14}$/
  return regex.test(phone)
}
