export const waitFor = (condition: () => boolean, timeout = 5000): Promise<void> => {
  return new Promise((resolve, reject) => {
    const startTime = Date.now()

    const check = () => {
      if (condition()) {
        resolve()
      } else if (Date.now() - startTime > timeout) {
        reject(new Error('Timeout waiting for condition'))
      } else {
        requestAnimationFrame(check)
      }
    }

    check()
  })
}
