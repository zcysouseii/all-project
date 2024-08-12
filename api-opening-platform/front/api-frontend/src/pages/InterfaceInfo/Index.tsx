import { PageContainer } from '@ant-design/pro-components';

import {
  getInterfaceInfoVoByIdUsingGet,
  invokeInterfaceInfoUsingPost,
} from '@/services/api-opening-platform-backend/interfaceInfoController';
import { useParams } from '@umijs/max';
import { Button, Card, Descriptions, Form, FormProps, message } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import { Divider } from 'rc-menu';
import React, { useEffect, useState } from 'react';

/**
 * 主页
 *
 * @constructor
 */
const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.InterfaceInfoVO>();
  const [invokeRes, setInvokeRes] = useState<any>();
  const [invokeLoading, setInvokeLoading] = useState(false);
  const params = useParams();

  // 使用async和await关键字可以把异步变成同步，调用完才获取返回值
  const loadData = async () => {
    if (!params.id) {
      message.error('参数不存在');
      return;
    }
    setLoading(true);
    try {
      const res = await getInterfaceInfoVoByIdUsingGet({
        id: Number(params.id),
      });
      setData(res.data);
    } catch (error: any) {
      message.error('请求失败，' + error.message);
    }
    setLoading(false);
  };

  // 页面首次加载的时候向后端发送请求
  useEffect(() => {
    loadData();
  }, []);

  const onFinish: FormProps<FieldType>['onFinish'] = async (values) => {
    if (!params.id) {
      message.error('接口不存在');
      return;
    }
    setInvokeLoading(true);
    try {
      const res = await invokeInterfaceInfoUsingPost({
        id: params.id,
        ...values,
      });
      setInvokeRes(res.data);
      message.success('请求成功');
    } catch (error: any) {
      message.error('请求失败，' + error.message);
    }
    setInvokeLoading(false);
  };

  return (
    <PageContainer title="查看接口文档">
      <Card>
        <Descriptions title={data?.name} column={1} extra={<Button>调用</Button>}>
          <Descriptions.Item label="接口状态">{data?.status ? '开启' : '关闭'}</Descriptions.Item>
          <Descriptions.Item label="描述">{data?.description}</Descriptions.Item>
          <Descriptions.Item label="请求地址">{data?.url}</Descriptions.Item>
          <Descriptions.Item label="请求方法">{data?.method}</Descriptions.Item>
          <Descriptions.Item label="请求参数">{data?.requestParams}</Descriptions.Item>
          <Descriptions.Item label="请求头">{data?.requestHeader}</Descriptions.Item>
          <Descriptions.Item label="响应头">{data?.responseHeader}</Descriptions.Item>
          <Descriptions.Item label="创建时间">{data?.createTime}</Descriptions.Item>
          <Descriptions.Item label="更新时间">{data?.updateTime}</Descriptions.Item>
        </Descriptions>
      </Card>
      <Card title="在线测试">
        <Form layout="vertical" name="invoke" onFinish={onFinish}>
          <Form.Item<FieldType> label="请求参数" name="userRequestParams">
            <TextArea />
          </Form.Item>
          <Form.Item wrapperCol={{ span: 16 }}>
            <Button type="primary" htmlType="submit">
              调用
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="调用接口" loading={invokeLoading}>
        {invokeRes}
      </Card>
    </PageContainer>
  );
};

export default Index;
