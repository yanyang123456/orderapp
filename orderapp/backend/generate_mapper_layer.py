from pathlib import Path

root = Path(r"c:\Users\lunxi\Desktop\orderapp\backend")
entity_dir = root / "src/main/java/com/orderapp/supplier/entity"
mapper_dir = root / "src/main/java/com/orderapp/supplier/mapper"
xml_dir = root / "src/main/resources/mapper"

schemas = [
    ("suppliers", "Supplier", [("id","Long"),("supplier_no","String"),("supplier_name","String"),("contact_mobile","String"),("contact_name","String"),("status","String"),("token","String"),("created_at","LocalDateTime"),("updated_at","LocalDateTime")]),
    ("supplier_auth_codes", "SupplierAuthCode", [("id","Long"),("mobile","String"),("code","String"),("expired_at","LocalDateTime"),("used","Boolean"),("created_at","LocalDateTime")]),
    ("supplier_dashboard_alerts", "SupplierDashboardAlert", [("id","Long"),("supplier_id","Long"),("alert_type","String"),("title","String"),("content","String"),("priority","String"),("status","String"),("created_at","LocalDateTime")]),
    ("supplier_wallet_settlements", "SupplierWalletSettlement", [("id","Long"),("supplier_id","Long"),("settlement_month","String"),("total_income","BigDecimal"),("withdrawable_amount","BigDecimal"),("frozen_amount","BigDecimal"),("order_amount","BigDecimal"),("delivery_fee","BigDecimal"),("installation_fee","BigDecimal"),("refund_deduction","BigDecimal"),("aftersale_deduction","BigDecimal"),("status","String"),("created_at","LocalDateTime")]),
    ("supplier_wallet_transactions", "SupplierWalletTransaction", [("id","Long"),("supplier_id","Long"),("settlement_id","Long"),("title","String"),("description","String"),("amount","BigDecimal"),("direction","String"),("biz_type","String"),("biz_no","String"),("created_at","LocalDateTime")]),
    ("supplier_products", "SupplierProduct", [("id","Long"),("supplier_id","Long"),("name","String"),("images","String"),("category_id","Long"),("category","String"),("description","String"),("material","String"),("color","String"),("size","String"),("model","String"),("style","String"),("price","BigDecimal"),("stock","Integer"),("spot_stock","Integer"),("presale_cycle_days","Integer"),("custom_cycle_days","Integer"),("min_order_quantity","Integer"),("delivery_areas","String"),("package_info","String"),("support_installation","Boolean"),("status","String"),("review_status","String"),("created_at","LocalDateTime"),("updated_at","LocalDateTime")]),
    ("supplier_quotes", "SupplierQuote", [("id","Long"),("supplier_id","Long"),("product_id","Long"),("current_price","BigDecimal"),("new_price","BigDecimal"),("reason","String"),("attachments","String"),("review_status","String"),("reject_reason","String"),("created_at","LocalDateTime"),("updated_at","LocalDateTime")]),
    ("supplier_fulfillments", "SupplierFulfillment", [("id","Long"),("supplier_id","Long"),("delivery_no","String"),("order_no","String"),("area","String"),("warehouse_id","Long"),("warehouse","String"),("customer_name","String"),("customer_address","String"),("appointment_date","LocalDate"),("appointment_time","String"),("item_summary","String"),("quantity","Integer"),("volume","BigDecimal"),("weight","BigDecimal"),("driver_name","String"),("driver_mobile","String"),("status","String"),("shipped_at","LocalDateTime"),("created_at","LocalDateTime"),("updated_at","LocalDateTime")]),
    ("supplier_installations", "SupplierInstallation", [("id","Long"),("supplier_id","Long"),("order_no","String"),("product_summary","String"),("customer_name","String"),("customer_address","String"),("installer_name","String"),("installer_mobile","String"),("appointment_date","LocalDate"),("appointment_time","String"),("status","String"),("photos","String"),("remark","String"),("completed_at","LocalDateTime"),("created_at","LocalDateTime"),("updated_at","LocalDateTime")]),
    ("supplier_aftersale_cases", "SupplierAftersaleCase", [("id","Long"),("supplier_id","Long"),("case_no","String"),("order_no","String"),("type","String"),("description","String"),("status","String"),("remaining_hours","Integer"),("opinion","String"),("solution","String"),("attachments","String"),("review_status","String"),("remark","String"),("created_at","LocalDateTime"),("updated_at","LocalDateTime")]),
    ("supplier_reviews", "SupplierReview", [("id","Long"),("supplier_id","Long"),("review_type","String"),("biz_id","Long"),("title","String"),("status","String"),("reject_reason","String"),("created_at","LocalDateTime"),("updated_at","LocalDateTime")]),
    ("supplier_analytics_daily", "SupplierAnalyticsDaily", [("id","Long"),("supplier_id","Long"),("stat_date","LocalDate"),("product_views","Integer"),("order_count","Integer"),("sales_amount","BigDecimal"),("return_rate","BigDecimal"),("aftersale_rate","BigDecimal"),("fulfillment_rate","BigDecimal"),("created_at","LocalDateTime")]),
    ("supplier_product_metrics", "SupplierProductMetric", [("id","Long"),("supplier_id","Long"),("product_id","Long"),("stat_date","LocalDate"),("views","Integer"),("orders","Integer"),("sales_amount","BigDecimal"),("created_at","LocalDateTime")]),
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
    props = ", ".join(f"#{{{camel(c)}, jdbcType={jdbc(t)}}}" for c, t in cols)
    no_id_cols = [(c,t) for c,t in cols if c != "id"]
    no_id_columns = ", ".join(c for c,_ in no_id_cols)
    no_id_props = ", ".join(f"#{{item.{camel(c)}, jdbcType={jdbc(t)}}}" for c,t in no_id_cols)
    has_status = any(c == "status" for c,_ in cols)
    lines = ["<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
             "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">",
             f"<mapper namespace=\"{ns}\">", ""]
    lines.append(f"    <resultMap id=\"BaseResultMap\" type=\"com.orderapp.supplier.entity.{class_name}\">")
    for c,t in cols:
        tag = "id" if c == "id" else "result"
        lines.append(f"        <{tag} column=\"{c}\" property=\"{camel(c)}\" jdbcType=\"{jdbc(t)}\" />")
    lines.append("    </resultMap>\n")
    lines.append(f"    <sql id=\"BaseColumnList\">{columns}</sql>\n")
    lines.append("    <insert id=\"insert\" parameterType=\"" + f"com.orderapp.supplier.entity.{class_name}" + "\" useGeneratedKeys=\"true\" keyProperty=\"id\">")
    lines.append(f"        INSERT INTO {table} ({', '.join(c for c,_ in no_id_cols)})")
    lines.append(f"        VALUES ({', '.join('#{' + camel(c) + ', jdbcType=' + jdbc(t) + '}' for c,t in no_id_cols)})")
    lines.append("    </insert>\n")
    lines.append("    <insert id=\"insertBatch\">")
    lines.append(f"        INSERT INTO {table} ({no_id_columns}) VALUES")
    lines.append("        <foreach collection=\"list\" item=\"item\" separator=\",\">")
    lines.append(f"            ({no_id_props})")
    lines.append("        </foreach>")
    lines.append("    </insert>\n")
    lines.append("    <update id=\"updateById\" parameterType=\"" + f"com.orderapp.supplier.entity.{class_name}" + "\">")
    lines.append(f"        UPDATE {table}")
    lines.append("        <set>")
    for c,t in no_id_cols:
        prop = camel(c)
        lines.append(f"            <if test=\"{prop} != null\">{c} = #{{{prop}, jdbcType={jdbc(t)}}},</if>")
    lines.append("        </set>")
    lines.append("        WHERE id = #{id, jdbcType=BIGINT}")
    lines.append("    </update>\n")
    lines.append("    <select id=\"selectByCondition\" resultMap=\"BaseResultMap\" parameterType=\"" + f"com.orderapp.supplier.entity.{class_name}" + "\">")
    lines.append("        SELECT <include refid=\"BaseColumnList\" />")
    lines.append(f"        FROM {table}")
    lines.append("        <where>")
    if has_status:
        lines.append("            <if test=\"status == null\">AND status != 'deleted'</if>")
    for c,t in cols:
        prop = camel(c)
        if t == "String":
            lines.append(f"            <if test=\"{prop} != null and {prop} != ''\">AND {c} = #{{{prop}, jdbcType={jdbc(t)}}}</if>")
        else:
            lines.append(f"            <if test=\"{prop} != null\">AND {c} = #{{{prop}, jdbcType={jdbc(t)}}}</if>")
    lines.append("        </where>")
    lines.append("        ORDER BY id DESC")
    lines.append("    </select>\n")
    lines.append("    <select id=\"selectById\" resultMap=\"BaseResultMap\">")
    where_status = " AND status != 'deleted'" if has_status else ""
    lines.append(f"        SELECT <include refid=\"BaseColumnList\" /> FROM {table} WHERE id = #{{id, jdbcType=BIGINT}}{where_status}")
    lines.append("    </select>\n")
    lines.append("    <select id=\"selectByIds\" resultMap=\"BaseResultMap\">")
    lines.append(f"        SELECT <include refid=\"BaseColumnList\" /> FROM {table}")
    lines.append("        WHERE id IN")
    lines.append("        <foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">#{id, jdbcType=BIGINT}</foreach>")
    if has_status:
        lines.append("        AND status != 'deleted'")
    lines.append("    </select>\n")
    if has_status:
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
