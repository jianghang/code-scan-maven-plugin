package com.github.common.enums;

public enum AlarmLevelEnum {
    /**
     * 警告级别
     */
    WARN("警告", "warn"),
    /**
     * 错误级别
     */
    ERROR("错误", "error");

    AlarmLevelEnum(String label, String value) {
        this.label = label;
        this.value = value;
    }

    private final String label;
    private final String value;

    public static AlarmLevelEnum getAlarmLevel(String value) {
        for (AlarmLevelEnum alarmLevelEnum : AlarmLevelEnum.values()) {
            if (alarmLevelEnum.getValue().equals(value)) {
                return alarmLevelEnum;
            }
        }
        return AlarmLevelEnum.WARN;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
