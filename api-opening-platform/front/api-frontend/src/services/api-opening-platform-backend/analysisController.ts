// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** getTopInvokeInterfaceInfo GET /api/analysis/top/interface/invoke */
export async function getTopInvokeInterfaceInfoUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListAnalysisInterfaceInfoVO_>(
    '/api/analysis/top/interface/invoke',
    {
      method: 'GET',
      ...(options || {}),
    },
  );
}
