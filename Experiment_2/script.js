const products = [
  { name: "Laptop", category: "electronics", price: 999 },
  { name: "Shirt", category: "clothing", price: 25 },
  { name: "Headphones", category: "electronics", price: 199 },
  { name: "Jeans", category: "clothing", price: 45 },
  { name: "Smartphone", category: "electronics", price: 699 },
];
document.getElementById("filter").addEventListener("change", (e) => {
  const selected = e.target.value;
  const filtered =
    selected === "all"
      ? products
      : products.filter((p) => p.category === selected);
  renderProducts(filtered);
});

function renderProducts(products) {
  const container = document.getElementById("products-container");
  if (products.length === 0) {
    container.innerHTML = `<p>No products found</p>`;
    return;
  }
  container.innerHTML = products
    .map(
      (p) => `
    <div class="product">
      <h3>${p.name}</h3>
      <p>$${p.price}</p>
    </div>
  `
    )
    .join("");
}

renderProducts(products);
