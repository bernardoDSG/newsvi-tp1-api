# remove-bom.ps1 - remove UTF-8 BOM (0xEF,0xBB,0xBF) from text files
$paths = @()
$paths += Get-ChildItem -Path .\src -Include *.java,*.properties,*.ps1 -Recurse -File -ErrorAction SilentlyContinue
$paths += Get-ChildItem -Path .\src\main\docker -Filter 'Dockerfile*' -Recurse -File -ErrorAction SilentlyContinue
$paths += Get-ChildItem -Path .\scripts -Include *.ps1 -Recurse -File -ErrorAction SilentlyContinue
foreach ($f in $paths) {
    try {
        $bytes = [System.IO.File]::ReadAllBytes($f.FullName)
        if ($bytes.Length -gt 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
            $newBytes = $bytes[3..($bytes.Length - 1)]
            [System.IO.File]::WriteAllBytes($f.FullName, $newBytes)
            Write-Host "Removed BOM: $($f.FullName)"
        }
    } catch {
        Write-Host "Failed BOM removal: $($f.FullName) -> $_"
    }
}
Write-Host "BOM removal completed"
