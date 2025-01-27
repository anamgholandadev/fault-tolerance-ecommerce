import http from "k6/http";

export const options = {
	executor: "ramping-arrival-rate",
	stages: [
		{ duration: "5m", target: 20000 },
	],
	thresholds: {
    http_req_failed: [{ threshold: 'rate<0.15', abortOnFail: true }],
  },
};

export default function () {
  const payload = JSON.stringify({
    productId: 1,
    userId: 2,
    ft: false,
  });
  const headers = { "Content-Type": "application/json" };
  const response = http.post("http://127.0.0.1:8080/ecommerce/buy", payload, {headers});
}

