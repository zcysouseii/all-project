import { type ProColumns, ProTable } from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React, {useEffect, useRef} from 'react';

export type Props = {
  values: API.InterfaceInfoVO
  columns: ProColumns<API.InterfaceInfoVO>[];
  onCancel: () => void;
  onSubmit: (values: API.InterfaceInfoVO) => Promise<void>;
  visible: boolean;
};
const UpdateModal: React.FC<Props> = (props) => {
  const { values, columns, visible, onCancel, onSubmit } = props;

  const formRef = useRef()

  useEffect(() => {
    if (formRef) {
      formRef.current?.setFieldsValue(values)
    }
  }, [values])

  return (
    <Modal
      footer={null}
      visible={visible}
      onCancel={() => {
        onCancel?.();
      }}
    >
      <ProTable
        type="form"
        formRef={formRef}
        columns={columns}
        form={{
          initialValues: values
        }}
        onSubmit={async (value) => {
          onSubmit?.(value);
        }}
      />
    </Modal>
  );
};
export default UpdateModal;
