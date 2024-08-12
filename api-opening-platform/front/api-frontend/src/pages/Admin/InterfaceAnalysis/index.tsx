import {PageContainer,} from '@ant-design/pro-components';
import '@umijs/max';
import React, {useEffect, useState} from 'react';
import ReactEcharts from 'echarts-for-react'
import {getTopInvokeInterfaceInfoUsingGet} from "@/services/api-opening-platform-backend/analysisController";

/**
 * 接口分析
 * @constructor
 */
const InterfaceAnalysis: React.FC = () => {

  const [data, setData] = useState<API.AnalysisInterfaceInfoVO[]>([]);
  const [loading, setLoading] = useState(true);

  // deps这个参数发生改变，这个函数就会重新出发，所以如果只是想他触发一次，就设置为空数组
  useEffect(() => {
    try {
      getTopInvokeInterfaceInfoUsingGet().then(res => {
        if (res.data) {
          setData(res.data);
          setLoading(false)
        }
      })
    } catch (e: any) {

    }
  }, [])

  // 映射
  const chartData = data.map(item => {
    return {
      value: item.totalNum,
      name: item.name,
    }
  })

  const option = {
    title: {
      text: '调用次数最多的接口TOP3',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '50%',
        data: chartData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  return (
    <PageContainer>
      <ReactEcharts showLoading={loading} option={option}/>
    </PageContainer>
  );
};
export default InterfaceAnalysis;
