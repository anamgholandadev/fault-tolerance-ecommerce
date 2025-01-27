import http from 'k6/http';
import { sleep } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";


export let options = {
  stages: [  
    { duration: '10s', target: 10 }, 
    { duration: '10s', target: 20 }, 
    { duration: '10s', target: 30 }, 
    { duration: '10s', target: 50 }, 
    { duration: '10s', target: 70 },  
    { duration: '10s', target: 90 },
    { duration: '10s', target: 100 },  
    { duration: '10s', target: 120 }, 
    { duration: '10s', target: 150 },  
    { duration: '10s', target: 170 },  
    { duration: '10s', target: 200 },  
    { duration: '10s', target: 220 },  
    { duration: '10s', target: 240 },  
    { duration: '10s', target: 260 },  
    { duration: '10s', target: 280 },  
    { duration: '10s', target: 300 },  
  ],  
};

export default function () {
  const url = 'http://127.0.0.1:8080/ecommerce/buy'; 
  const payload = JSON.stringify({
    productId: '1', 
    userId: "2",
    ft: true,
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  http.post(url, payload, params); 
  sleep(1);
}

export function handleSummary(data) {
    return {
      "indexTestCapacityV1.html": htmlReport(data),
    };
}
