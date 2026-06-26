package net.centroweg.gerenciamentocompras;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"net.centroweg.gerenciamentocompras.modules", "net.centroweg.gerenciamentocompras.shared.cloudinary"})
public class AllUnitTestsSuite {
}
