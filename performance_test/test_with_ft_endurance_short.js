import http from 'k6/http';
import { check } from 'k6';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';

export const options = {
    stages: [
        { duration: '10s', target: 50 },
        { duration: '10s', target: 150 },
        { duration: '2m', target: 300 },
        { duration: '10s', target: 100 },
        { duration: '10s', target: 0 },
    ],
};

export default function () {
    const payload = JSON.stringify({
        productId: '1',
        userId: '2',
        ft: true
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    const res = http.post('http://localhost:8080/ecommerce/buy', payload, params);

    check(res, {
        'Status is 200': (r) => r.status === 200,
    });
}
export function handleSummary(data) {
    return {
        'summary_with_ft_short.html': htmlReport(data),
    };
}