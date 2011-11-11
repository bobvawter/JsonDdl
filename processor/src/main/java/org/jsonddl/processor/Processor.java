package org.jsonddl.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.jsonddl.generator.Generator;
import org.jsonddl.generator.Generator.Collector;

@SupportedAnnotationTypes("org.jsonddl.processor.GenerateFrom")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class Processor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element elt : roundEnv.getElementsAnnotatedWith(GenerateFrom.class)) {
      final PackageElement pelt = (PackageElement) elt;
      String packageName = pelt.getQualifiedName().toString();
      for (String fileName : elt.getAnnotation(GenerateFrom.class).value()) {
        try {
          FileObject obj = processingEnv.getFiler().getResource(StandardLocation.SOURCE_PATH,
              packageName, fileName);

          new Generator().generate(obj.openInputStream(), packageName, new Collector() {

            @Override
            public void println(String message) {
              processingEnv.getMessager().printMessage(Kind.NOTE, message);
            }

            @Override
            public void println(String format, Object... args) {
              println(String.format(format, args));
            }

            @Override
            public OutputStream writeImplementation(String packageName, String simpleName)
                throws IOException {
              JavaFileObject obj = processingEnv.getFiler().createSourceFile(
                  packageName + "." + simpleName);
              return obj.openOutputStream();
            }
          });

        } catch (IOException e) {
          processingEnv.getMessager().printMessage(Kind.ERROR, e.getMessage(), pelt);
          e.printStackTrace();
        }
      }
    }
    return false;
  }
}
