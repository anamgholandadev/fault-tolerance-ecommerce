import http from 'k6/http';
import { sleep } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";


export let options = {
  stages: [  
    { duration: '1m', target: 10 }, 
    { duration: '1m', target: 20 }, 
    { duration: '1m', target: 30 }, 
    { duration: '1m', target: 50 }, 
    { duration: '1m', target: 70 },  
    { duration: '1m', target: 90 },
    { duration: '1m', target: 100 },  
    { duration: '1m', target: 120 }, 
    { duration: '1m', target: 150 },  
    { duration: '1m', target: 170 },  
    { duration: '1m', target: 200 },  
    { duration: '1m', target: 220 },  
    { duration: '1m', target: 240 },  
    { duration: '1m', target: 260 },  
    { duration: '1m', target: 280 },  
    { duration: '1m', target: 300 },  
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
      "indexTestCapacityV2.html": htmlReport(data),
    };
}
