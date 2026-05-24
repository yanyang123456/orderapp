from pathlib import Path

root = Path(r"c:\Users\lunxi\Desktop\orderapp\backend")
entity_dir = root / "src/main/java/com/orderapp/supplier/entity"
mapper_dir = root / "src/main/java/com/orderapp/supplier/mapper"
xml_dir = root / "src/main/resources/mapper"

schemas = [
    ("client_users", "ClientUser", [("id", "Long"), ("user_no", "String"), ("mobile", "String"), ("nickname", "String"), ("avatar", "String"), ("gender", "String"), ("level", "String"), ("points", "Integer"), ("status", "String"), ("token", "String"), ("created_at", "LocalDateTime"), ("updated_at", "LocalDateTime")]),
    ("client_auth_codes", "ClientAuthCode", [("id", "Long"), ("mobile", "String"), ("code", "String"), ("expired_at", "LocalDateTime"), ("used", "Boolean"), ("created_at", "LocalDateTime")]),
    ("client_addresses", "ClientAddress", [("id", "Long"), ("user_id", "Long"), ("receiver_name", "String"), ("receiver_mobile", "String"), ("province", "String"), ("city", "String"), ("district", "String"), ("detail_address", "String"), ("is_default", "Boolean"), ("status", "String"), ("created_at", "LocalDateTime"), ("updated_at", "LocalDateTime")]),
    ("client_cart_items", "ClientCartItem", [("id", "Long"), ("user_id", "Long"), ("supplier_id", "Long"), ("product_id", "Long"), ("quantity", "Integer"), ("support_installation", "Boolean"), ("selected", "Boolean"), ("status", "String"), ("created_at", "LocalDateTime"), ("updated_at", "LocalDateTime")]),
    ("client_orders", "ClientOrder", [("id", "Long"), ("order_no", "String"), ("user_id", "Long"), ("supplier_id", "Long"), ("address_id", "Long"), ("receiver_name", "String"), ("receiver_mobile", "String"), ("receiver_address", "String"), ("delivery_method", "String"), ("appointment_date", "LocalDate"), ("appointment_time", "String"), ("product_amount", "BigDecimal"), ("delivery_fee", "BigDecimal"), ("installation_fee", "BigDecimal"), ("discount_amount", "BigDecimal"), ("pay_amount", "BigDecimal"), ("pay_status", "String"), ("order_status", "String"), ("delivery_status", "String"), ("installation_status", "String"), ("remark", "String"), ("paid_at", "LocalDateTime"), ("received_at", "LocalDateTime"), ("completed_at", "LocalDateTime"), ("created_at", "LocalDateTime"), ("updated_at", "LocalDateTime")]),
    ("client_order_items", "ClientOrderItem", [("id", "Long"), ("order_id", "Long"), ("order_no", "String"), ("supplier_id", "Long"), ("product_id", "Long"), ("product_name", "String"), ("product_image", "String"), ("category", "String"), ("material", "String"), ("color", "String"), ("size", "String"), ("model", "String"), ("price", "BigDecimal"), ("quantity", "Integer"), ("amount", "BigDecimal"), ("support_installation", "Boolean"), ("created_at", "LocalDateTime")]),
    ("client_payments", "ClientPayment", [("id", "Long"), ("payment_no", "String"), ("order_id", "Long"), ("order_no", "String"), ("user_id", "Long"), ("pay_channel", "String"), ("pay_amount", "BigDecimal"), ("status", "String"), ("paid_at", "LocalDateTime"), ("transaction_no", "String"), ("created_at", "LocalDateTime"), ("updated_at", "LocalDateTime")]),
    ("client_aftersales", "ClientAftersale", [("id", "Long"), ("case_no", "String"), ("supplier_case_id", "Long"), ("order_id", "Long"), ("order_no", "String"), ("user_id", "Long"), ("supplier_id", "Long"), ("product_id", "Long"), ("type", "String"), ("description", "String"), ("solution", "String"), ("photos", "String"), ("supplier_opinion", "String"), ("status", "String"), ("review_status", "String"), ("completed_at", "LocalDateTime"), ("created_at", "LocalDateTime"), ("updated_at", "LocalDateTime")]),
    ("client_favorites", "ClientFavorite", [("id", "Long"), ("user_id", "Long"), ("supplier_id", "Long"), ("product_id", "Long"), ("status", "String"), ("created_at", "LocalDateTime"), ("updated_at", "LocalDateTime")]),
    ("client_browse_history", "ClientBrowseHistory", [("id", "Long"), ("user_id", "Long"), ("supplier_id", "Long"), ("product_id", "Long"), ("product_name", "String"), ("product_image", "String"), ("viewed_at", "LocalDateTime"), ("created_at", "LocalDateTime")]),
]

def camel(name):
    parts = name.split('_')
    return parts[0] + ''.join(p[:1].upper() + p[1:] for p in parts[1:])

def cap(s):
    return s[:1].upper() + s[1:]

def jdbc(java_type):
    return {
        "Long": "BIGINT",
        "Integer": "INTEGER",
        "Boolean": "BOOLEAN",
        "BigDecimal": "DECIMAL",
        "LocalDate": "DATE",
        "LocalDateTime": "TIMESTAMP",
    }.get(java_type, "VARCHAR")

def entity_code(class_name, cols):
    imports = set()
    for _, typ in cols:
        if typ == "BigDecimal": imports.add("java.math.BigDecimal")
        if typ == "LocalDate": imports.add("java.time.LocalDate")
        if typ == "LocalDateTime": imports.add("java.time.LocalDateTime")
    lines = ["package com.orderapp.supplier.entity;", ""]
    for imp in sorted(imports):
        lines.append(f"import {imp};")
    if imports:
        lines.append("")
    lines += [f"public class {class_name} {{"]
    for col, typ in cols:
        lines.append(f"    private {typ} {camel(col)};")
    lines.append("")
    for col, typ in cols:
        prop = camel(col)
        lines.append(f"    public {typ} get{cap(prop)}() {{")
        lines.append(f"        return {prop};")
        lines.append("    }")
        lines.append("")
        lines.append(f"    public void set{cap(prop)}({typ} {prop}) {{")
        lines.append(f"        this.{prop} = {prop};")
        lines.append("    }")
        lines.append("")
    lines.append("}")
    return "\n".join(lines)

def mapper_code(class_name):
    return f"""package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.{class_name};
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface {class_name}Mapper extends BaseMapper<{class_name}> {{
}}
"""

def xml_code(table, class_name, cols):
    ns = f"com.orderapp.supplier.mapper.{class_name}Mapper"
    columns = ", ".join(c for c, _ in cols)
    no_id_cols = [(c, t) for c, t in cols if c != "id"]
    no_id_columns = ", ".join(c for c, _ in no_id_cols)
    lines = ["<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
             "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">",
             f"<mapper namespace=\"{ns}\">", ""]
    lines.append(f"    <resultMap id=\"BaseResultMap\" type=\"com.orderapp.supplier.entity.{class_name}\">")
    for c, t in cols:
        tag = "id" if c == "id" else "result"
        lines.append(f"        <{tag} column=\"{c}\" property=\"{camel(c)}\" jdbcType=\"{jdbc(t)}\" />")
    lines.append("    </resultMap>\n")
    lines.append(f"    <sql id=\"BaseColumnList\">{columns}</sql>\n")
    lines.append("    <insert id=\"insert\" parameterType=\"com.orderapp.supplier.entity." + class_name + "\" useGeneratedKeys=\"true\" keyProperty=\"id\">")
    lines.append(f"        INSERT INTO {table} ({no_id_columns})")
    lines.append(f"        VALUES ({', '.join('#{' + camel(c) + ', jdbcType=' + jdbc(t) + '}' for c, t in no_id_cols)})")
    lines.append("    </insert>\n")
    lines.append("    <insert id=\"insertBatch\">")
    lines.append(f"        INSERT INTO {table} ({no_id_columns}) VALUES")
    lines.append("        <foreach collection=\"list\" item=\"item\" separator=\",\">")
    lines.append(f"            ({', '.join('#{item.' + camel(c) + ', jdbcType=' + jdbc(t) + '}' for c, t in no_id_cols)})")
    lines.append("        </foreach>")
    lines.append("    </insert>\n")
    lines.append("    <update id=\"updateById\" parameterType=\"com.orderapp.supplier.entity." + class_name + "\">")
    lines.append(f"        UPDATE {table}")
    lines.append("        <set>")
    for c, t in no_id_cols:
        prop = camel(c)
        lines.append(f"            <if test=\"{prop} != null\">{c} = #{{{prop}, jdbcType={jdbc(t)}}},</if>")
    lines.append("        </set>")
    lines.append("        WHERE id = #{id, jdbcType=BIGINT}")
    lines.append("    </update>\n")
    lines.append("    <select id=\"selectByCondition\" resultMap=\"BaseResultMap\" parameterType=\"com.orderapp.supplier.entity." + class_name + "\">")
    lines.append("        SELECT <include refid=\"BaseColumnList\" />")
    lines.append(f"        FROM {table}")
    lines.append("        <where>")
    if any(c == "status" for c, _ in cols):
        lines.append("            <if test=\"status == null\">AND status != 'deleted'</if>")
    for c, t in cols:
        prop = camel(c)
        if t == "String":
            lines.append(f"            <if test=\"{prop} != null and {prop} != ''\">AND {c} = #{{{prop}, jdbcType={jdbc(t)}}}</if>")
        else:
            lines.append(f"            <if test=\"{prop} != null\">AND {c} = #{{{prop}, jdbcType={jdbc(t)}}}</if>")
    lines.append("        </where>")
    lines.append("        ORDER BY id DESC")
    lines.append("    </select>\n")
    lines.append("    <select id=\"selectById\" resultMap=\"BaseResultMap\">")
    status_cond = " AND status != 'deleted'" if any(c == "status" for c, _ in cols) else ""
    lines.append(f"        SELECT <include refid=\"BaseColumnList\" /> FROM {table} WHERE id = #{{id, jdbcType=BIGINT}}{status_cond}")
    lines.append("    </select>\n")
    lines.append("    <select id=\"selectByIds\" resultMap=\"BaseResultMap\">")
    lines.append(f"        SELECT <include refid=\"BaseColumnList\" /> FROM {table}")
    lines.append("        WHERE id IN")
    lines.append("        <foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">#{id, jdbcType=BIGINT}</foreach>")
    if any(c == "status" for c, _ in cols):
        lines.append("        AND status != 'deleted'")
    lines.append("    </select>\n")
    if any(c == "status" for c, _ in cols):
        lines.append("    <update id=\"deleteById\">")
        lines.append(f"        UPDATE {table} SET status = 'deleted' WHERE id = #{{id, jdbcType=BIGINT}}")
        lines.append("    </update>\n")
        lines.append("    <update id=\"deleteByIds\">")
        lines.append(f"        UPDATE {table} SET status = 'deleted' WHERE id IN")
        lines.append("        <foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">#{id, jdbcType=BIGINT}</foreach>")
        lines.append("    </update>")
    else:
        lines.append("    <delete id=\"deleteById\">")
        lines.append(f"        DELETE FROM {table} WHERE id = #{{id, jdbcType=BIGINT}}")
        lines.append("    </delete>\n")
        lines.append("    <delete id=\"deleteByIds\">")
        lines.append(f"        DELETE FROM {table} WHERE id IN")
        lines.append("        <foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">#{id, jdbcType=BIGINT}</foreach>")
        lines.append("    </delete>")
    lines.append("\n</mapper>\n")
    return "\n".join(lines)

for table, class_name, cols in schemas:
    (entity_dir / f"{class_name}.java").write_text(entity_code(class_name, cols), encoding="utf-8")
    (mapper_dir / f"{class_name}Mapper.java").write_text(mapper_code(class_name), encoding="utf-8")
    (xml_dir / f"{class_name}Mapper.xml").write_text(xml_code(table, class_name, cols), encoding="utf-8")
