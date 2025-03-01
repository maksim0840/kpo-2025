# Занятие 8. DDD

## Цель занятия
- Разобраться что такое DDD. Дополнить проект работой с файлами, а в частности формированием отчетов разных форматов.
## Требования к реализации
1.
## Тестирование
1. .
## Задание на доработку
- 
## Пояснения к реализации
Добавьте зависимость Jackson в build.gradle, для включение адаптера обычных классов в json формат:
```
implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
```

Добавьте перечисление для форматов отчета:

public enum ReportFormat {
JSON,
MARKDOWN
}

Создайте абстрактный класс экспортера:

package hse.kpo.export.reports;

import hse.kpo.domains.Report;
import java.io.IOException;
import java.io.Writer;

public interface ReportExporter {
void export(Report report, Writer writer) throws IOException;
}

Реализуйте экспорт для типов [json](/report.json) и [MARKDOWN](/report.MD):
package hse.kpo.export.reports.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hse.kpo.domains.Report;
import hse.kpo.export.reports.ReportExporter;

import java.io.IOException;
import java.io.Writer;

public class JsonReportExporter implements ReportExporter {
private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void export(Report report, Writer writer) throws IOException {
        objectMapper.writeValue(writer, report);
    }
}

package hse.kpo.export.reports.impl;

import hse.kpo.domains.Report;
import hse.kpo.export.reports.ReportExporter;

import java.io.IOException;
import java.io.Writer;

public class MarkdownReportExporter implements ReportExporter {
@Override
public void export(Report report, Writer writer) throws IOException {
writer.write("# " + report.title() + "\n\n");
writer.write(report.content());
writer.flush();
}
}

Реализуйте фабрику экспортеров:

package hse.kpo.factories;

import hse.kpo.enums.ReportFormat;
import hse.kpo.export.reports.ReportExporter;
import hse.kpo.export.reports.impl.JsonReportExporter;
import hse.kpo.export.reports.impl.MarkdownReportExporter;
import org.springframework.stereotype.Component;

@Component
public class ReportExporterFactory {
public ReportExporter create(ReportFormat format) {
return switch (format) {
case JSON -> new JsonReportExporter();
case MARKDOWN -> new MarkdownReportExporter();
default -> throw new IllegalArgumentException("Unsupported format: " + format);
};
}
}

Интегрируйте экспорт отчетов в класс Hse:

package hse.kpo.facade;

// ... импорты ...

@Component
@RequiredArgsConstructor
public class Hse {
// ... существующие зависимости ...
private final ReportExporterFactory reportExporterFactory;

    public void exportReport(ReportFormat format, Writer writer) {
        Report report = salesObserver.buildReport();
        ReportExporter exporter = reportExporterFactory.create(format);

        try {
            exporter.export(report, writer);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    // Старый метод для обратной совместимости
    public String generateReport() {
        return salesObserver.buildReport().toString();
    }
}

Добавить использование в главный тест/main:

// Экспорт в консоль в формате Markdown
hse.exportReport(ReportFormat.MARKDOWN, new PrintWriter(System.out));
// Экспорт в файл в формате MARKDOWN
try (FileWriter fileWriter = new FileWriter("report.MD")) {
hse.exportReport(ReportFormat.MARKDOWN, fileWriter);
}

// Экспорт в файл в формате JSON
try (FileWriter fileWriter = new FileWriter("report.json")) {
hse.exportReport(ReportFormat.JSON, fileWriter);
}

Задание экспорта транспорта:
Необходимо добавить общий интерфейс для машин и катамаранов и реализовать его методы.
public interface Transport {
boolean isCompatible(Customer customer);
int getVin(); 
String getEngineType();
String getTransportType();
}

Сигнатура транспортых экспортеров:
public interface TransportExporter {
void export(List<Transport> transports, Writer writer) throws IOException;
}

<details> 
<summary>Ссылки</summary>
1. 
</details>