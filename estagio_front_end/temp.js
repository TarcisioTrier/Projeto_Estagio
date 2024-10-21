function generateCNPJ() {
  const randomDigits = (length) => {
    let digits = '';
    for (let i = 0; i < length; i++) {
      digits += Math.floor(Math.random() * 10);
    }
    return digits;
  };

  const calculateCheckDigit = (base) => {
    const weights = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
    let sum = 0;
    for (let i = 0; i < base.length; i++) {
      sum += base[i] * weights[i + (weights.length - base.length)];
    }
    const remainder = sum % 11;
    return remainder < 2 ? 0 : 11 - remainder;
  };

  let base = randomDigits(8) + '0001';
  let firstCheckDigit = calculateCheckDigit(base);
  let secondCheckDigit = calculateCheckDigit(base + firstCheckDigit);

  return base + firstCheckDigit + secondCheckDigit;
}

console.log(generateCNPJ());
console.log(generateCNPJ());
console.log(generateCNPJ());
console.log(generateCNPJ());
console.log(generateCNPJ());
